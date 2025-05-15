package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.ColumnTableSaveMetaData
import ru.tutko.micro.logibot.telegram.model.data.MetadataTableData
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.Pageable
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.data.TableColumnData
import ru.tutko.micro.logibot.telegram.model.data.TableData
import ru.tutko.micro.logibot.telegram.model.data.TableDataColumnType
import ru.tutko.micro.logibot.telegram.model.data.TableDataPaginate
import ru.tutko.micro.logibot.telegram.model.data.TableSaveMetaData
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.TableService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class TableHandler(
	val organizationService: OrganizationService,
	val telegramKeyboard: TelegramKeyboard,
	val tableService: TableService,
) {


	@CallbackMapping(CallbackQueryEnum.PAGINATE_GET_TABLES)
	fun callbackGetTables(request: Request, organization: OrganizationPaginate): Response {

		val tables = tableService.getTablesByOrganization(organization.orgId, page = organization.pageable.page)

		val tableButtons: List<List<Pair<String, CallbackData<Payload>>>> = tables.content.map { table ->
			listOf(
				table.tableName!! to CallbackData(
					CallbackQueryEnum.GET_TABLE,
					TableData(organization.orgId, table.id!!)
				)
			)
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.GET_ORGANIZATION,
				OrganizationId(organization.orgId)
			)
		)

		navigationButtons.add(
			"Создать новую таблицу" to CallbackData(
				CallbackQueryEnum.CREATE_TABLE,
				OrganizationId(organization.orgId)
			)
		)

		if (tables.hasPrevious()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_TABLES,
					organization.pageable.decreasePage()
				)
			)
		}
		if (tables.hasNext()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_TABLES,
					organization.pageable.increasePage()
				)
			)
		}

		val buttons =
			telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + tableButtons)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text = "Ваши таблицы, выберите что бы работать с ними"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.GET_TABLE)
	fun callbackGetTable(request: Request, tableData: TableData): Response {

		val table = tableService.getTable(tableData.tableId)

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.PAGINATE_GET_TABLES, OrganizationPaginate(
					tableData.organizationId,
					Pageable()
				)
			)
		)

		navigationButtons.add("Добавить запись" to CallbackData(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN, TableSaveMetaData(tableData)))

		navigationButtons.add(
			"Посмотреть все записи" to CallbackData(
				CallbackQueryEnum.GET_DATA_TABLE_COLUMN,
				TableDataPaginate(tableData)
			)
		)

		navigationButtons.add("Редактировать таблицу" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN,
			TableDataPaginate(tableData)))

		navigationButtons.add(
			"Редактировать роли таблицы" to CallbackData(
				CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS,
				TableDataPaginate(tableData)
			)
		)

		navigationButtons.add("Выгрузить записи" to CallbackData(CallbackQueryEnum.GET_REPORT_TABLE, tableData))

		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}", navigationButtons)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text = "Таблица ${table.tableName}, выберите что хотите сделать"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.EDIT_TABLE_COLUMN)
	fun editTable(request: Request, tableDataPaginate: TableDataPaginate): Response {
		val tablesColumn = tableService.getTableColumns(tableDataPaginate.tableData.tableId, tableDataPaginate.pageable.page)

		val columnButtons: List<List<Pair<String, CallbackData<Payload>>>> = tablesColumn.content.map { column ->
			listOf(
				column.columnName!! to CallbackData(
					CallbackQueryEnum.GET_COLUMN_TABLE,
					TableColumnData(tableDataPaginate.tableData.organizationId, tableDataPaginate.tableData.tableId, column.id!!)
				)
			)
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.GET_TABLE, tableDataPaginate.tableData))

		navigationButtons.add("Добавить колонку" to CallbackData(CallbackQueryEnum.ADD_COLUMN_TABLE, tableDataPaginate.tableData))

		if (tablesColumn.hasPrevious()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.EDIT_TABLE_COLUMN,
					tableDataPaginate.pageable.decreasePage()
				)
			)
		}
		if (tablesColumn.hasNext()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.EDIT_TABLE_COLUMN,
					tableDataPaginate.pageable.increasePage()
				)
			)
		}
		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + columnButtons)

		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Выберите колонку с которой хотите взаимодействовать!"
					replyMarkup = buttons
				}
			)
		)

	}

	@CallbackMapping(CallbackQueryEnum.GET_COLUMN_TABLE)
	fun getColumn(request: Request, tableColumnData: TableColumnData): Response {
		val column = tableService.getTableColumn(tableColumnData.columnId);

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN, TableDataPaginate(TableData(tableColumnData.organizationId, tableColumnData.tableId))))
		navigationButtons.add("Удалить" to CallbackData(CallbackQueryEnum.DELETE_COLUMN, tableColumnData))
		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}", navigationButtons)

		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Колонка ${column.columnName}"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.DELETE_COLUMN)
	fun deleteColumn(request: Request, tableColumnData: TableColumnData): Response {
		tableService.deleteTableColumn(tableColumnData.columnId)
		return editTable(request, TableDataPaginate(TableData(tableColumnData.organizationId, tableColumnData.tableId)))
	}

	@CallbackMapping(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN)
	fun addColumn(request: Request, tableSaveMetaData: TableSaveMetaData): Response {
		val tablesColumn = tableService.getTableColumns(tableSaveMetaData.tableData.tableId, tableSaveMetaData.pageable.page)

		val columnButtons: List<List<Pair<String, CallbackData<Payload>>>> = tablesColumn.content.map { column ->
			val value = tableSaveMetaData.filledTableRow?.get(column.id!!)
			val label = if (value != null && value.toString().isNotBlank()) {
				"${column.columnName!!}: $value"
			} else {
				"${column.columnName!!} ❌"
			}

			listOf(
				label to CallbackData(
					CallbackQueryEnum.ADD_METADATA_COLUMN,
					ColumnTableSaveMetaData(column.id!!, tableSaveMetaData)
				)
			)
		}


		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.GET_TABLE, tableSaveMetaData.tableData
			)
		)
		if (tablesColumn.hasPrevious()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.ADD_DATA_TABLE_COLUMN,
					tableSaveMetaData.pageable.decreasePage()
				)
			)
		}
		if (tablesColumn.hasNext()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.ADD_DATA_TABLE_COLUMN,
					tableSaveMetaData.pageable.increasePage()
				)
			)
		}
		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + columnButtons)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text =
						"Выберите колонку в которую хотите записать данные!"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.ADD_METADATA_COLUMN)
	fun addMetadata(request: Request, columnTableSaveMetaData: ColumnTableSaveMetaData): Response {
		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text = "Введите данные для колонки или отмените ввод"
					replyMarkup =
						telegramKeyboard.createInlineKeyboardRow("${request.userId}", "Отмена" to CallbackData(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN, columnTableSaveMetaData.tableSaveMetaData))
				}
			),
			inputType = CallbackData<Payload>(InputEnum.SET_DATA_COLUMN, columnTableSaveMetaData)
		)
	}

	@InputMapping(InputEnum.SET_DATA_COLUMN)
	fun setDataColumnInput(request: Request, columnTableSaveMetaData: ColumnTableSaveMetaData): Response {
		val columnText = request.update.message.text
		val tableSaveMetaData = columnTableSaveMetaData.tableSaveMetaData
		tableSaveMetaData.filledTableRow[columnTableSaveMetaData.columnId] = columnText

		val tablesColumn = tableService.getTableColumns(tableSaveMetaData.tableData.tableId, tableSaveMetaData.pageable.page)

		val columnButtons: List<List<Pair<String, CallbackData<Payload>>>> = tablesColumn.content.map { column ->
			val value = tableSaveMetaData.filledTableRow.get(column.id!!)
			val label = if (value != null && value.toString().isNotBlank()) {
				"${column.columnName!!}: $value"
			} else {
				"${column.columnName!!} ❌"
			}

			listOf(
				label to CallbackData(
					CallbackQueryEnum.ADD_METADATA_COLUMN,
					ColumnTableSaveMetaData(column.id!!, tableSaveMetaData)
				)
			)
		}


		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Отмена" to CallbackData(
				CallbackQueryEnum.GET_TABLE, tableSaveMetaData.tableData
			)
		)
		if (tablesColumn.hasPrevious()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.ADD_DATA_TABLE_COLUMN,
					tableSaveMetaData.pageable.decreasePage()
				)
			)
		}
		if (tablesColumn.hasNext()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.ADD_DATA_TABLE_COLUMN,
					tableSaveMetaData.pageable.increasePage()
				)
			)
		}
		val saveButtons = mutableListOf<Pair<String, CallbackData<Payload>>>("Сохранить" to CallbackData(CallbackQueryEnum.SAVE_METADATA, tableSaveMetaData))



		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + columnButtons + listOf(saveButtons))

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Выберите колонку в которую хотите записать данные!"
					replyMarkup = buttons
				}
			),
			clearWaitingForInput = true
		)
	}

	@CallbackMapping(CallbackQueryEnum.SAVE_METADATA)
	fun saveMetadata(request: Request, tableSaveMetadata:  TableSaveMetaData): Response {
		val metadata = tableService.addMetadataTableColumn(tableSaveMetadata.tableData.tableId, request.userId, tableSaveMetadata.filledTableRow)

		return callbackGetTable(request, tableSaveMetadata.tableData)
	}

	@CallbackMapping(CallbackQueryEnum.GET_DATA_TABLE_COLUMN)
	fun getColumnMetadata(request: Request, tableDataPaginate: TableDataPaginate): Response {
		val metadatas = tableService.getTableDataMetadata(tableDataPaginate.tableData.tableId, page = tableDataPaginate.pageable.page)

		val dataButtons: List<List<Pair<String, CallbackData<Payload>>>> = metadatas.content.map { data ->
			listOf(
				data.createdAt.toString() to CallbackData(
					CallbackQueryEnum.GET_DATA_TABLE,
					MetadataTableData(tableDataPaginate.tableData.organizationId, tableDataPaginate.tableData.tableId, data.id!!)
				)
			)
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.GET_TABLE, tableDataPaginate.tableData
			)
		)
		if (metadatas.hasPrevious()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.ADD_DATA_TABLE_COLUMN,
					tableDataPaginate.pageable.decreasePage()
				)
			)
		}
		if (metadatas.hasNext()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.ADD_DATA_TABLE_COLUMN,
					tableDataPaginate.pageable.increasePage()
				)
			)
		}

		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + dataButtons)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text =
						"Выберите колонку в которую хотите посмотреть данные!"
					replyMarkup = buttons
				}
			),
		)
	}

	@CallbackMapping(CallbackQueryEnum.GET_DATA_TABLE)
	fun getDataTable(request: Request, metadataTableData: MetadataTableData): Response {
		val data = tableService.getMetadataEntry(metadataTableData.tableId, metadataTableData.dataId)

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.GET_DATA_TABLE_COLUMN, TableDataPaginate(TableData(metadataTableData.organizationId, metadataTableData.tableId))
			)
		)
		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons))

		val text = buildString {
			appendLine("📝 *Данные по записи*")
			appendLine()

			data.data.entries.forEach { (columnId, cell) ->
				appendLine("• *${cell.cellName}* (${cell.fieldType.value}): ${cell.value}")
			}
		}

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					this.text = text
					parseMode = "Markdown"
					replyMarkup = buttons
				}

			),
		)
	}

	@CallbackMapping(CallbackQueryEnum.GET_REPORT_TABLE)
	fun getReportTable(request: Request, tableDataPaginate: TableDataPaginate): Response {
		TODO()
	}

	@InputMapping(InputEnum.CREATE_TABLE)
	fun createTableInput(request: Request, organizationId: OrganizationId): Response {
		val tableName = request.update.message.text
		val table = tableService.createTable(organizationId.orgId, tableName)

		val tableData = TableData(organizationId.orgId, table.id!!)

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.PAGINATE_GET_TABLES, OrganizationPaginate(
					tableData.organizationId,
					Pageable()
				)
			)
		)

		navigationButtons.add("Добавить запись" to CallbackData(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN, TableSaveMetaData(tableData)))

		navigationButtons.add(
			"Посмотреть все записи" to CallbackData(
				CallbackQueryEnum.GET_DATA_TABLE_COLUMN,
				TableDataPaginate(tableData)
			)
		)

		navigationButtons.add("Редактировать таблицу" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN,
			TableDataPaginate(tableData)))

		navigationButtons.add(
			"Редактировать роли таблицы" to CallbackData(
				CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS,
				TableDataPaginate(tableData)
			)
		)

		navigationButtons.add("Выгрузить записи" to CallbackData(CallbackQueryEnum.GET_REPORT_TABLE, tableData))

		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}", navigationButtons)

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Таблица ${table.tableName}, выберите что хотите сделать"
					replyMarkup = buttons
				}
			),
			clearWaitingForInput = true,
		)
	}

	@CallbackMapping(CallbackQueryEnum.CREATE_TABLE)
	fun createTable(request: Request, organizationId: OrganizationId): Response {
		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text = "Введите название таблицы или отмените ввод '/cancel'"
					replyMarkup =
						telegramKeyboard.createInlineKeyboardRow("${request.userId}", "Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData<Payload>(InputEnum.CREATE_TABLE, organizationId)
		)
	}

	@CallbackMapping(CallbackQueryEnum.ADD_COLUMN_TABLE)
	fun addColumnToTable(request: Request, tableData: TableData): Response {

		val columnTypeButtons: List<List<Pair<String, CallbackData<Payload>>>> = ColumnTypeEnum.entries.toTypedArray().map { columnType ->
			listOf(
				columnType.value to CallbackData(CallbackQueryEnum.SET_NAME_COLUMN, TableDataColumnType(tableData, columnType)),
			)
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add("Отмена" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN, TableDataPaginate(tableData)))

		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + columnTypeButtons)

		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Выберите тип колонки которую хотите создать"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.SET_NAME_COLUMN)
	fun setColumnToTable(request: Request, tableDataColumnType: TableDataColumnType): Response {
		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text = "Введите название колонки или отмените ввод '/cancel'"
					replyMarkup =
						telegramKeyboard.createInlineKeyboardRow("${request.userId}", "Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData<Payload>(InputEnum.SET_NAME_COLUMN, tableDataColumnType)
		)
	}


	@InputMapping(InputEnum.SET_NAME_COLUMN)
	fun setNameColumn(request: Request, tableDataColumnType: TableDataColumnType): Response {
		val columnName = request.update.message.text
		val table = tableService.addTableColumn(tableDataColumnType.tableData.tableId , columnName, tableDataColumnType.columnType)

		val tableData = tableDataColumnType.tableData

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.PAGINATE_GET_TABLES, OrganizationPaginate(
					tableData.organizationId,
					Pageable()
				)
			)
		)

		navigationButtons.add("Добавить запись" to CallbackData(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN, TableSaveMetaData(tableData)))

		navigationButtons.add(
			"Посмотреть все записи" to CallbackData(
				CallbackQueryEnum.GET_DATA_TABLE_COLUMN,
				TableDataPaginate(tableData)
			)
		)

		navigationButtons.add("Редактировать таблицу" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN,
			TableDataPaginate(tableData)))

		navigationButtons.add(
			"Редактировать роли таблицы" to CallbackData(
				CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS,
				TableDataPaginate(tableData)
			)
		)

		navigationButtons.add("Выгрузить записи" to CallbackData(CallbackQueryEnum.GET_REPORT_TABLE, tableData))

		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}", navigationButtons)

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Таблица ${table.tableName}, выберите что хотите сделать"
					replyMarkup = buttons
				}
			)
		)
	}
}