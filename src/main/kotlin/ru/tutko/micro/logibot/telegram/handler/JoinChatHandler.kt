package ru.tutko.micro.logibot.telegram.handler

import org.springframework.beans.factory.annotation.Value
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.mapping.ChatMemberMapping
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.MyChatMemberMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.dto.ChatDto
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.Paginate
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.ChatService
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil
import kotlin.Pair
import kotlin.String
import kotlin.collections.List

@Handlers
class JoinChatHandler(
	@Value("\${telegrambots.bots[0].username}") val myBotUsername: String,
	private val organizationService: OrganizationService,
	private val chatService: ChatService,
	private val telegramKeyboard: TelegramKeyboard
) {

	@MyChatMemberMapping()
	fun handleJoinBot(request: Request): Response {
		val organizations = organizationService.getOrganizationsByUserId(request.userId, 0)
		val organizationButtons: List<Pair<String, CallbackData<Payload>>> = organizations.content.map { org ->
			org.name!! to CallbackData(CallbackQueryEnum.SET_CHAT_ORGANIZATION, OrganizationId(org.id!!))
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()
		navigationButtons.add("Создать новую организацию" to CallbackData(CallbackQueryEnum.SET_CHAT_CREATE_ORGANIZATION))
		if (organizations.hasNext()) {
			navigationButtons.add("->" to CallbackData(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION, Paginate(1)))
		}

		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}",navigationButtons + organizationButtons)

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Добрый день, Для начала выдайте мне " +
							"[права администратор](https://help.chatplace.io/en/articles/10995326-%D0%BA%D0%B0%D0%BA-%D1%81%D0%B4%D0%B5%D0%BB%D0%B0%D1%82%D1%8C-%D0%B1%D0%BE%D1%82%D0%B0-%D0%B0%D0%B4%D0%BC%D0%B8%D0%BD%D0%B8%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%BE%D1%80%D0%BE%D0%BC-telegram-%D0%BA%D0%B0%D0%BD%D0%B0%D0%BB%D0%B0-%D0%B8%D0%BB%D0%B8-%D1%87%D0%B0%D1%82%D0%B0)" +
							"после этого выберите организацию"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION)
	fun handlePaginateJoinBot(request: Request): Response {
		val paginate = request.data?.data as Paginate

		val organizations = organizationService.getOrganizationsByUserId(request.userId, paginate.page)

		val organizationButtons: List<Pair<String, CallbackData<Payload>>> = organizations.content.map { org ->
			org.name!! to CallbackData(CallbackQueryEnum.SET_CHAT_ORGANIZATION, OrganizationId(org.id!!))
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()
		navigationButtons.add("Создать новую организацию" to CallbackData(CallbackQueryEnum.SET_CHAT_CREATE_ORGANIZATION))
		if (organizations.hasPrevious()) {
			navigationButtons.add("<-" to CallbackData(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION, Paginate(paginate.page - 1)))
		}
		if (organizations.hasNext()) {
			navigationButtons.add("->" to CallbackData(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION, Paginate(paginate.page - 1)))
		}

		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}", navigationButtons + organizationButtons)

		return Response(
			botApiMethods = listOf(
				EditMessageReplyMarkup().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.SET_CHAT_ORGANIZATION)
	fun handleSetChatOrganization(request: Request): Response {
		val organizationId =
			request.data?.data as OrganizationId
		val chat = UpdateUtil(request.update).getChat()
		val organizationDto = organizationService.getOrganizationById(organizationId.orgId)

		val chatDto = chatService.createChat(
			ChatDto(
				externalChatId = request.chatId,
				organization = ChatDto.OrganizationDto(id = organizationDto?.id),
				type = ChatTypeEnum.fromValue(chat.type),
				title = chat.title,
				chatUsername = chat.userName,
			)
		)

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Этот чат (${chatDto.title}) принадлежит организации (${organizationDto?.name})\nТеперь все кого вы добавите в этот чат будут принадлежать этой организации\nПропишите /start что бы продолжить работу"
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.SET_CHAT_CREATE_ORGANIZATION)
	fun handleSetChatCreateOrganizationCallback(request: Request): Response {
		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					this.chatId = request.chatId.toString()
					text = "Введите название организации"
					replyMarkup =
						telegramKeyboard.createInlineKeyboardRow("${request.userId}","Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData<Payload>(InputEnum.SET_CHAT_CREATE_ORGANIZATION)
		)
	}

	@InputMapping(InputEnum.SET_CHAT_CREATE_ORGANIZATION)
	fun handleSetChatCreateOrganizationInput(request: Request): Response {
		val chat = UpdateUtil(request.update).getChat()

		val organizationName = request.update.message.text
		val organizationDto = organizationService.createOrganization(organizationName, request.userId)

		val chatDto = chatService.createChat(
			ChatDto(
				externalChatId = request.chatId,
				organization = ChatDto.OrganizationDto(id = organizationDto?.id),
				type = ChatTypeEnum.fromValue(chat.type),
				title = chat.title,
				chatUsername = chat.userName,
			)
		)

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text =
						"Этот чат (${chatDto.title}) принадлежит организации (${organizationDto?.name})\nТеперь все кого вы добавите в этот чат будут принадлежать этой организации\nПропишите /start что бы продолжить работу"
				}
			)
		)
	}

	@ChatMemberMapping()
	fun handleJoinUser(request: Request): Response {
		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Добро пожаловать в организацию напиши мне в [личный чат](https://t.me/${myBotUsername} '/start' что бы начать работу)"
				}
			)
		)
	}
}