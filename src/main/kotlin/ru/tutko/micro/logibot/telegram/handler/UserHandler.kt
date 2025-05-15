package ru.tutko.micro.logibot.telegram.handler

import org.springframework.beans.factory.annotation.Value
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.Pageable
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.data.UserOrganizationData
import ru.tutko.micro.logibot.telegram.model.data.UserOrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.UserOrganizationRoleData
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.RoleService
import ru.tutko.micro.logibot.telegram.service.UserService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class UserHandler(
	private val userService: UserService,
	private val organizationService: OrganizationService,
	private val roleService: RoleService,
	@Value("\${telegrambots.bots[0].username}") val myBotUsername: String,
	private val telegramKeyboard: TelegramKeyboard
) {

	@CallbackMapping(CallbackQueryEnum.PAGINATE_GET_USERS)
	fun callbackQueryGetUsers(request: Request, organizationPaginate: OrganizationPaginate): Response {

		val users = userService.getUsersByOrganizationIdAndPermissions(
			organizationPaginate.orgId, listOf(
				PermissionAccessEnum.JOINER
			), false, organizationPaginate.pageable.page
		)

		val userIdButtons: List<List<Pair<String, CallbackData<Payload>>>> = users.content.map { user ->
			listOf(
				user.firstName!! to CallbackData(
					CallbackQueryEnum.GET_USER,
					user.id?.let { UserOrganizationData(user.id!!, organizationPaginate.orgId) })
			)
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.GET_ORGANIZATION,
				OrganizationId(organizationPaginate.orgId)
			)
		)

		navigationButtons.add(
			"Заявки" to CallbackData(
				CallbackQueryEnum.PAGINATE_GET_JOINERS, OrganizationPaginate(
					organizationPaginate.orgId,
					Pageable(0)
				)
			)
		)

		if (users.hasPrevious()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_USERS,
					organizationPaginate.pageable.increasePage()
				)
			)
		}
		if (users.hasNext()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_USERS,
					organizationPaginate.pageable.decreasePage()
				)
			)
		}

		val buttons =
			telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + userIdButtons)


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

	@CallbackMapping(CallbackQueryEnum.PAGINATE_GET_JOINERS)
	fun callbackQueryGetJoiners(request: Request, organizationPaginate: OrganizationPaginate): Response {
		val users = userService.getUsersByOrganizationIdAndPermissions(
			organizationPaginate.orgId, listOf(
				PermissionAccessEnum.JOINER
			), true, organizationPaginate.pageable.page
		)

		val userIdButtons: List<List<Pair<String, CallbackData<Payload>>>> = users.content.map { user ->
			listOf(
				user.firstName!! to CallbackData(
					CallbackQueryEnum.GET_USER,
					user.id?.let { UserOrganizationData(user.id!!, organizationPaginate.orgId) })
			)
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.GET_ORGANIZATION,
				OrganizationId(organizationPaginate.orgId)
			)
		)

		navigationButtons.add(
			"Сотрудники" to CallbackData(
				CallbackQueryEnum.PAGINATE_GET_USERS, OrganizationPaginate(
					organizationPaginate.orgId,
					Pageable(0)
				)
			)
		)

		navigationButtons.add(
			"Создать приглашение" to CallbackData(
				CallbackQueryEnum.CREATE_JOIN_REQUEST,
				OrganizationId(organizationPaginate.orgId)
			)
		)

		if (users.hasPrevious()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_JOINERS,
					organizationPaginate.pageable.increasePage()
				)
			)
		}
		if (users.hasNext()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_JOINERS,
					organizationPaginate.pageable.decreasePage()
				)
			)
		}

		val buttons =
			telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + userIdButtons)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text = "Пользователи желающие присоединится  к вашей организации"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.CREATE_JOIN_REQUEST)
	fun callbackQueryCreateJoinRequest(request: Request, organizationId: OrganizationId): Response {
		val organization = organizationService.getOrganizationById(organizationId.orgId)

		val token = "org_${organization.id}"
		val inviteLink = "https://t.me/${myBotUsername.substring(1)}?start=$token"

		return Response(
			botApiMethods = listOf(
				SendMessage().apply {
					chatId = request.chatId.toString()
					text = "Вот ссылка что бы пригласить пользователя в организацию $inviteLink"
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.GET_USER)
	fun callbackQueryGetUser(request: Request, userOrganizationData: UserOrganizationData): Response {
		val userOrganizationLink = userService.getUserOrganizationLinkByUserOrganizationData(userOrganizationData.organizationId, userOrganizationData.userId)

		val role = roleService.getRoleByUserOrganization(userOrganizationData.organizationId, request.userId)

		if (userOrganizationLink.user!!.externalUserId != request.userId) {
			val buttons = telegramKeyboard.createInlineKeyboardRow(
				"${request.userId}",
				listOfNotNull(
					("Назад" to CallbackData(
						CallbackQueryEnum.PAGINATE_GET_USERS,
						OrganizationPaginate(userOrganizationData.organizationId, Pageable(0))
					)),
					TelegramKeyboard.checkAccessCreateButton(
						role, CallbackQueryEnum.MANAGE_USER_ROLE,
						UserOrganizationPaginate(userOrganizationData, Pageable(0)), "Изменить роль сотрудника"
					),
				)
			)
			return Response(
				botApiMethods = listOf(
					EditMessageText().apply {
						messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
						chatId = request.chatId.toString()
						text =
							"Выбран сотрудник ${userOrganizationLink.user!!.firstName} ${userOrganizationLink.user!!.lastName}\nРоль сотрудника ${userOrganizationLink.role!!.roleName}"
						replyMarkup = buttons
					}
				)
			)
		} else{
			val buttons = telegramKeyboard.createInlineKeyboardRow(
				"${request.userId}",
				listOfNotNull(
					("Назад" to CallbackData(
						CallbackQueryEnum.PAGINATE_GET_USERS,
						OrganizationPaginate(userOrganizationData.organizationId, Pageable(0))
					)),

				)
			)
			return Response(
				botApiMethods = listOf(
					EditMessageText().apply {
						messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
						chatId = request.chatId.toString()
						text =
							"Вы выбрали себя ${userOrganizationLink.user!!.firstName} ${userOrganizationLink.user!!.lastName}\nРоль сотрудника ${userOrganizationLink.role!!.roleName}"
						replyMarkup = buttons
					}
				)
			)
		}
	}


	@CallbackMapping(CallbackQueryEnum.MANAGE_USER_ROLE)
	fun callbackQueryManageUserRole(request: Request, userOrganizationPaginate: UserOrganizationPaginate): Response {
		val userOrganizationLink = userService.getUserOrganizationLinkByUserOrganizationData(userOrganizationPaginate.userOrganizationData.organizationId, userOrganizationPaginate.userOrganizationData.userId)

		val roles = organizationService.getRolesOrganization(userOrganizationPaginate.userOrganizationData.organizationId, page = userOrganizationPaginate.pageable.page)

		val rolesDataButtons: List<List<Pair<String, CallbackData<Payload>>>> = roles.content.map { role ->
			listOf(role.roleName!! to CallbackData(CallbackQueryEnum.SET_ROLE_USER, role.id?.let { UserOrganizationRoleData(
				userOrganizationLink.user!!.id!!, userOrganizationLink.organization!!.id!!, it)
			}))
		}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.GET_USER, userOrganizationPaginate.userOrganizationData))

		if (roles.hasPrevious()) {
			navigationButtons.add("Вперёд ➡️" to CallbackData(CallbackQueryEnum.MANAGE_USER_ROLE, userOrganizationPaginate.pageable.increasePage()))
		}
		if (roles.hasNext()) {
			navigationButtons.add("⬅️ Назад" to CallbackData(CallbackQueryEnum.MANAGE_USER_ROLE, userOrganizationPaginate.pageable.decreasePage()))
		}

		val buttons = telegramKeyboard.createInlineKeyboard("${request.userId}",listOf(navigationButtons) + rolesDataButtons)


		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text =
						"Выбран сотрудник ${userOrganizationLink.user!!.firstName} ${userOrganizationLink.user!!.lastName}\nРоль сотрудника ${userOrganizationLink.role!!.roleName}"
					replyMarkup = buttons
				}
			)
		)
	}

	@CallbackMapping(CallbackQueryEnum.SET_ROLE_USER)
	fun callbackQuerySetUserRole(request: Request, userOrganizationRoleData: UserOrganizationRoleData): Response {
		userService.updateUserRoleByOrganization(userOrganizationRoleData.organizationId, userOrganizationRoleData.userId, userOrganizationRoleData.roleId)

		val userOrganizationLink = userService.getUserOrganizationLinkByUserOrganizationData(userOrganizationRoleData.organizationId, userOrganizationRoleData.userId)

		val role = roleService.getRoleByUserOrganization(userOrganizationRoleData.organizationId, request.userId)

		val buttons = telegramKeyboard.createInlineKeyboardRow("${request.userId}",
			listOfNotNull(
				("Назад" to CallbackData(CallbackQueryEnum.PAGINATE_GET_USERS, OrganizationPaginate(userOrganizationRoleData.organizationId, Pageable(0)))),
				TelegramKeyboard.checkAccessCreateButton(role, CallbackQueryEnum.MANAGE_USER_ROLE,
					UserOrganizationPaginate(UserOrganizationData(userOrganizationRoleData.userId, userOrganizationRoleData.organizationId), Pageable(0)), "Изменить роль сотрудника"),
			)
		)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text =
						"Выбран сотрудник ${userOrganizationLink.user!!.firstName} ${userOrganizationLink.user!!.lastName}\nРоль сотрудника ${userOrganizationLink.role!!.roleName}"
					replyMarkup = buttons
				}
			)
		)
	}
}