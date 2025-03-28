package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.tutko.micro.logibot.telegram.annotation.mapping.ChatMemberMapping
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.MyChatMemberMapping
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.ChatMemberEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.MyChatMemberEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.util.TelegramUtil

@Handlers
class JoinChatHandler(
	private val organizationService: OrganizationService
) {

	@MyChatMemberMapping(MyChatMemberEnum.MEMBER)
	fun handleJoinBot(request: Request): Response {
		val organizations = organizationService.getOrganizationsByUserId(request.userId)
		if (organizations.isEmpty()) {
			return Response(
				botApiMethods = listOf(
					SendMessage().apply {
						chatId = request.chatId.toString()
						text = "Введите название организации"
						replyMarkup =
							TelegramUtil.createInlineKeyboard("Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
					}
				),
				inputType = CallbackData(InputEnum.CREATE_ORGANIZATION),
			)
		}

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Выберите организацию которую хотите присвоить этому чату или создайте новую"
					replyMarkup = TelegramUtil.createInlineKeyboard(
						organizations.map { org ->
							org.name!! to (CallbackData(CallbackQueryEnum.BOT_JOIN_ADD_ORGANIZATION, "orgId" to org.id!!))
						},
						listOf(
							"Создать новую организацию" to CallbackData(CallbackQueryEnum.CREATE_ORGANIZATION)
						)
					)
				}
			)
		)
	}

	@ChatMemberMapping(ChatMemberEnum.MEMBER)
	fun handleJoinUser(request: Request): Response {
		return Response(
			botApiMethods = listOf(
				SendMessage()
			)
		)
	}
}