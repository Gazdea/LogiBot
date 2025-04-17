package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.Role
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.RoleService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class RoleHandler(
	private val roleService: RoleService,
	private val organizationService: OrganizationService
) {

	@CallbackMapping(CallbackQueryEnum.PAGINATE_GET_ROLES)
	fun getRolesOrganizations(request: Request): Response {
		val organizationPaginate = request.data?.getData<OrganizationPaginate>() ?: throw ValidationException("Не найдены параметры")

		val roles = organizationService.getRolesOrganization(organizationPaginate.organizationId, page = organizationPaginate.paginate.page)

		val roleButtons = roles.content.map { role ->
			listOf(role.roleName!! to CallbackData(CallbackQueryEnum.GET_ROLE, role.id?.let { Role(it) }))
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData>>()

		navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.GET_ORGANIZATION, OrganizationId(organizationPaginate.organizationId)))

		navigationButtons.add("Создать новую роль" to CallbackData(CallbackQueryEnum.CREATE_ROLE, OrganizationId(organizationPaginate.organizationId)))

		if (roles.hasPrevious()) {
			navigationButtons.add("->" to CallbackData(CallbackQueryEnum.PAGINATE_GET_ROLES, organizationPaginate.paginate.increasePage()))
		}
		if (roles.hasNext()) {
			navigationButtons.add("<-" to CallbackData(CallbackQueryEnum.PAGINATE_GET_ROLES, organizationPaginate.paginate.decreasePage()))
		}

		val buttons = UpdateUtil.createInlineKeyboard(
			listOf(navigationButtons) + roleButtons
		)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text = "Роли в вашей организации"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.GET_ROLE)
	fun getRole(request: Request): Response {
		val role = request.data?.getData<Role>() ?: throw ValidationException()

		val roleDto = roleService.getRole(role.roleId)
		//TODO

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text = "Выбрана роль ${roleDto.roleName}"
				}
			)
		)
	}


}