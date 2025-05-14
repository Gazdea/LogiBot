package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.Pageable
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.data.TableData
import ru.tutko.micro.logibot.telegram.model.data.TableDataPaginate
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
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

		navigationButtons.add("Добавить запись" to CallbackData(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN, tableData))

		navigationButtons.add(
			"Посмотреть все записи" to CallbackData(
				CallbackQueryEnum.GET_DATA_TABLE_COLUMN,
				tableData
			)
		)

		navigationButtons.add("Редактировать таблицу" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN,
			TableDataPaginate(tableData)))

		navigationButtons.add(
			"Редактировать роли таблицы" to CallbackData(
				CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS,
				tableData
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
					tableDataPaginate.tableData
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


}