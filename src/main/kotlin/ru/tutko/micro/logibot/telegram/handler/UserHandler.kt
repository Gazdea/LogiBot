package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.User
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.service.UserService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class UserHandler(
	private val userService: UserService,
) {

	@CallbackMapping(CallbackQueryEnum.PAGINATE_GET_USERS)
	fun callbackQueryGetUsers(request: Request): Response {
		val organizationPaginate = request.data?.getData<OrganizationPaginate>()
			?: throw ValidationException("Не найдена OrganizationPaginate")

		val users = userService.getUsersByOrganization(organizationPaginate.organizationId, organizationPaginate.paginate.page)

		val userButtons = users.content.map { user ->
			listOf(user.firstName!! to CallbackData(CallbackQueryEnum.GET_USER, user.id?.let { User(it) }))
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData>>()

		navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.GET_ORGANIZATION, OrganizationId(organizationPaginate.organizationId)))

		if (users.hasPrevious()) {
			navigationButtons.add("->" to CallbackData(CallbackQueryEnum.PAGINATE_GET_USERS, organizationPaginate.paginate.increasePage()))
		}
		if (users.hasNext()) {
			navigationButtons.add("<-" to CallbackData(CallbackQueryEnum.PAGINATE_GET_USERS, organizationPaginate.paginate.decreasePage()))
		}

		val buttons = UpdateUtil.createInlineKeyboard(
			listOf(navigationButtons) + userButtons
		)


		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text = "Сотрудники в вашей организации"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.GET_USER)
	fun callbackQueryGetUser(request: Request): Response {
		val userData = request.data?.getData<User>() ?: throw ValidationException("Не найден userData")

		val user = userService.getUserById(userData.userId)

		// TODO
		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text = "Выбрана роль ${user.firstName} ${user.lastName}"
				}
			)
		)
	}

}