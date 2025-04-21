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
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class CreateOrganizationHandler(
	private val organizationService: OrganizationService,
	private val telegramKeyboard: TelegramKeyboard
) {

//	@CommandMapping(CommandEnum.CREATE_ORGANIZATION)
//	fun commandCreateOrganization(request: Request): Response {
//		return Response(
//			botApiMethods = listOf(
//				SendMessage().apply {
//					chatId = request.chatId.toString()
//					text = "Введите название организации"
//					replyMarkup =
//						UpdateUtil.createInlineKeyboard("Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
//				}
//			),
//			inputType = CallbackData(InputEnum.CREATE_ORGANIZATION))
//	}

	@InputMapping(InputEnum.CREATE_ORGANIZATION)
	fun callbackQueryCreateOrganization(request: Request): Response {
		val organizationName = request.update.message.text
		organizationService.createOrganization(organizationName, request.userId)
		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Организация $organizationName создана!\nДля продолжения работы с ботом создайте групповой чат и пригласите туда меня"
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
					text = "Введите название организации или отмените '/cancel'"
					replyMarkup =
						telegramKeyboard.createInlineKeyboardRow("${request.userId}", "Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData<Payload>(InputEnum.CREATE_ORGANIZATION)
		)
	}
}