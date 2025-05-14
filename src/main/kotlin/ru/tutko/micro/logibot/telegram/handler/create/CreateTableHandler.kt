package ru.tutko.micro.logibot.telegram.handler.create

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.Pageable
import ru.tutko.micro.logibot.telegram.model.data.Paginate
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.data.TableData
import ru.tutko.micro.logibot.telegram.model.data.TableDataColumnType
import ru.tutko.micro.logibot.telegram.model.data.TableDataPaginate
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.RoleService
import ru.tutko.micro.logibot.telegram.service.TableService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class CreateTableHandler(
	private val organizationService: OrganizationService,
	private val roleService: RoleService,
	private val telegramKeyboard: TelegramKeyboard,
	private val tableService: TableService,
) {

	@CallbackMapping(CallbackQueryEnum.CREATE_TABLE)
	fun createTable(request: Request, organizationId: OrganizationId): Response {
		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId
					chatId = request.chatId.toString()
					text = "Введите название организации или отмените ввод '/cancel'"
					replyMarkup =
						telegramKeyboard.createInlineKeyboardRow("${request.userId}", "Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData<Payload>(InputEnum.CREATE_TABLE, organizationId)
		)
	}

	@InputMapping(InputEnum.CREATE_TABLE)
	fun createTableInput(request: Request, organizationId: OrganizationId): Response {
		val tableName = request.update.message.text
		val organization = organizationService.getOrganizationById(organizationId.orgId)
		val table = tableService.createTable(organization.id!!, tableName)

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		val tableData = TableData(organizationId.orgId, table.id!!)

		navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.PAGINATE_GET_TABLES, OrganizationPaginate(organizationId.orgId,
			Pageable())))

		navigationButtons.add("Добавить запись" to CallbackData(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN, TableDataPaginate(tableData)))

		navigationButtons.add("Посмотреть все записи" to CallbackData(CallbackQueryEnum.GET_DATA_TABLE_COLUMN, tableData))

		navigationButtons.add("Редактировать таблицу" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN, tableData))

		navigationButtons.add("Редактировать роли таблицы" to CallbackData(CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS, tableData))

		navigationButtons.add("Выгрузить записи" to CallbackData(CallbackQueryEnum.GET_REPORT_TABLE, tableData))

		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}", navigationButtons)

		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Таблица ${table.tableName} создана!"
					replyMarkup = buttons
				}
			)
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

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		val tableData = TableData(tableDataColumnType.tableData.organizationId, table.id!!)

		navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.PAGINATE_GET_TABLES, OrganizationPaginate(tableDataColumnType.tableData.organizationId,
			Pageable())))

		navigationButtons.add("Добавить запись" to CallbackData(CallbackQueryEnum.ADD_DATA_TABLE_COLUMN, TableDataPaginate(tableData)))

		navigationButtons.add("Посмотреть все записи" to CallbackData(CallbackQueryEnum.GET_DATA_TABLE_COLUMN, tableData))

		navigationButtons.add("Редактировать таблицу" to CallbackData(CallbackQueryEnum.EDIT_TABLE_COLUMN, tableData))

		navigationButtons.add("Редактировать роли таблицы" to CallbackData(CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS, tableData))

		navigationButtons.add("Выгрузить записи" to CallbackData(CallbackQueryEnum.GET_REPORT_TABLE, tableData))

		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}", navigationButtons)

		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Таблица ${table.tableName} создана!"
					replyMarkup = buttons
				}
			)
		)
	}

}