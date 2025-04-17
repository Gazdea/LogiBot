package ru.tutko.micro.logibot.telegram.handler.create

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.RoleService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class CreateRoleHandler(
	private val roleService: RoleService,
) {

	@CallbackMapping(CallbackQueryEnum.CREATE_ROLE)
	fun callbackCreateRole(request: Request): Response {
		val organizationId = request.data?.getData<OrganizationId>() ?: throw ValidationException()
		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId
					chatId = request.chatId.toString()
					text = "Введите название роли или отмените '/cancel'"
					replyMarkup =
						UpdateUtil.createInlineKeyboardRow("Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
				}
			),
			inputType = CallbackData(InputEnum.CREATE_ROLE, organizationId)
		)
	}

	@InputMapping(InputEnum.CREATE_ROLE)
	fun updateRole(request: Request): Response {
		val organizationId = request.data?.getData<OrganizationId>() ?: throw ValidationException()
		val roleName = UpdateUtil(request.update).getMessage()?.text ?: throw ValidationException()

		val role = roleService.createRole(organizationId.orgId, roleName)

		return Response(
			clearWaitingForInput = true,
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Роль ${role.roleName} создана теперь вы можете управлять ролью"
				}
			)
		)

	}
}