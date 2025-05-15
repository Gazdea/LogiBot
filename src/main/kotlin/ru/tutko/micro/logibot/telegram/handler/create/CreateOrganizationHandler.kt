package ru.tutko.micro.logibot.telegram.handler.create

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.CommandMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.Pageable
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.RoleService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class CreateOrganizationHandler(
	private val organizationService: OrganizationService,
	private val roleService: RoleService,
	private val telegramKeyboard: TelegramKeyboard
) {

	@InputMapping(InputEnum.CREATE_ORGANIZATION)
	fun callbackQueryCreateOrganization(request: Request): Response {
		val organizationName = request.update.message.text
		val organization = organizationService.createOrganization(organizationName, request.userId)

		val role = roleService.getRoleByUserOrganization(organization.id!!, request.userId)

		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}",
			listOfNotNull(
				("Назад" to CallbackData(CallbackQueryEnum.PAGINATE_ORGANIZATIONS, Pageable(0))),
//                TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.SETTINGS,
//                    null, "Настройки"),
				TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_ROLES,
					OrganizationPaginate(organization.id!!, Pageable(0)), "Роли организации"),
				TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_USERS,
					OrganizationPaginate(organization.id!!, Pageable(0)), "Сотрудники организации"),
				TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_TABLES,
					OrganizationPaginate(organization.id!!, Pageable(0)), "Таблицы организации"),
//                TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.PAGINATE_GET_CHATS,
//                    OrganizationPaginate(organizationId.orgId, Paginate(0)), "Чаты организации"),
//                TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.LOGS_ORGANIZATION,
//                    organizationId, "Логи организации"),
				TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.REPORT_ORGANIZATION,
					OrganizationId(organization.id!!), "Отчеты организации")
			))

		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Организация $organizationName создана!\nДля продолжения работы с ботом создайте групповой чат и пригласите туда меня"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.CREATE_ORGANIZATION)
	fun callbackQueryUpdateOrganization(request: Request): Response {
		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()!!.messageId
					chatId = request.chatId.toString()
					text = "Введите название организации или отмените ввод '/cancel'"
					replyMarkup =
						telegramKeyboard.createInlineKeyboardRow("${request.userId}", "Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData<Payload>(InputEnum.CREATE_ORGANIZATION)
		)
	}
}