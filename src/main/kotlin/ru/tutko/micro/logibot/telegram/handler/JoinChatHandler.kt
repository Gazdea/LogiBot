package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.tutko.micro.logibot.telegram.annotation.mapping.ChatMemberMapping
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.MyChatMemberMapping
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.dto.ChatDto
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.Paginate
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.ChatService
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class JoinChatHandler(
	private val organizationService: OrganizationService,
	private val chatService: ChatService,
) {

	@MyChatMemberMapping()
	fun handleJoinBot(request: Request): Response {
		val organizations = organizationService.getOrganizationsByUserId(request.userId, 0)

//		val organizationButtons = organizations.content.map { org ->
//			org.name!! to CallbackData(CallbackQueryEnum.SET_CHAT_ORGANIZATION, OrganizationId(org.id!!))
//		}
//
//		val navigationButtons = if (organizations.hasNext()) {
//			listOf("Вперёд" to CallbackData(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION, PaginateOrganizations(1)))
//		} else {
//			emptyList()
//		}
//
//		val createOrganizationButton = listOf(
//			"Создать новую организацию" to CallbackData(CallbackQueryEnum.SET_CHAT_CREATE_ORGANIZATION)
//		)
//
//		val buttons = UpdateUtil.createInlineKeyboard(
//			navigationButtons,
//			organizationButtons,
//			createOrganizationButton
//		)
		val organizationButtons = organizations.content.map { org ->
			listOf(org.name!! to CallbackData(CallbackQueryEnum.SET_CHAT_ORGANIZATION, OrganizationId(org.id!!)))
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData>>()
		navigationButtons.add("Создать новую организацию" to CallbackData(CallbackQueryEnum.SET_CHAT_CREATE_ORGANIZATION))
		if (organizations.hasNext()) {
			navigationButtons.add("->" to CallbackData(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION, Paginate(1)))
		}

		val buttons = UpdateUtil.createInlineKeyboard(
			listOf(navigationButtons) + organizationButtons
		)

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Добрый день, выберите какой организации должен принадлежать этот чат или создайте новую"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION)
	fun handlePaginateJoinBot(request: Request): Response {
		val paginate = request.data?.getData<Paginate>() ?: throw ValidationException()

		val organizations = organizationService.getOrganizationsByUserId(request.userId, paginate.page)

		val organizationButtons = organizations.content.map { org ->
			listOf(org.name!! to CallbackData(CallbackQueryEnum.SET_CHAT_ORGANIZATION, OrganizationId(org.id!!)))
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData>>()
		navigationButtons.add("Создать новую организацию" to CallbackData(CallbackQueryEnum.SET_CHAT_CREATE_ORGANIZATION))
		if (organizations.hasPrevious()) {
			navigationButtons.add("<-" to CallbackData(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION, Paginate(paginate.page - 1)))
		}
		if (organizations.hasNext()) {
			navigationButtons.add("->" to CallbackData(CallbackQueryEnum.PAGINATE_SET_CHAT_ORGANIZATION, Paginate(paginate.page - 1)))
		}

		val buttons = UpdateUtil.createInlineKeyboard(
			listOf(navigationButtons) + organizationButtons
		)

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Добрый день, выберите какой организации должен принадлежать этот чат или создайте новую"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.SET_CHAT_ORGANIZATION)
	fun handleSetChatOrganization(request: Request): Response {
		val organizationId =
			request.data?.getData<OrganizationId>() ?: throw ValidationException("Не найдено поле OrganizationId")
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
						UpdateUtil.createInlineKeyboard("Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData(InputEnum.SET_CHAT_CREATE_ORGANIZATION)
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
					text = "к нам зашел пользователь "
				}
			)
		)
	}
}