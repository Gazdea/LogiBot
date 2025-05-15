package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.data.RoleData
import ru.tutko.micro.logibot.telegram.model.data.OrganizationPaginate
import ru.tutko.micro.logibot.telegram.model.data.Pageable
import ru.tutko.micro.logibot.telegram.model.data.Paginate
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.data.RoleTableData
import ru.tutko.micro.logibot.telegram.model.data.TableData
import ru.tutko.micro.logibot.telegram.model.data.TableDataPaginate
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.service.RoleService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class RoleHandler(
	private val roleService: RoleService,
	private val organizationService: OrganizationService,
	private val telegramKeyboard: TelegramKeyboard
) {

	@CallbackMapping(CallbackQueryEnum.PAGINATE_GET_ROLES)
	fun getRolesOrganizations(request: Request, organizationPaginate: OrganizationPaginate): Response {

		val roles = organizationService.getRolesOrganization(
			organizationPaginate.orgId,
			page = organizationPaginate.pageable.page
		)

		val roleDataButtons: List<List<Pair<String, CallbackData<Payload>>>> = roles.content.map { role ->
			listOf(
				role.roleName!! to CallbackData(
					CallbackQueryEnum.GET_ROLE,
					role.id?.let { RoleData(organizationPaginate.orgId, roleId = it) })
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
			"Создать новую роль" to CallbackData(
				CallbackQueryEnum.CREATE_ROLE,
				OrganizationId(organizationPaginate.orgId)
			)
		)

		if (roles.hasPrevious()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_ROLES,
					organizationPaginate.pageable.increasePage()
				)
			)
		}
		if (roles.hasNext()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_ROLES,
					organizationPaginate.pageable.decreasePage()
				)
			)
		}

		val buttons =
			telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + roleDataButtons)

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

		@CallbackMapping(CallbackQueryEnum.GET_ROLE, CallbackQueryEnum.UPDATE_ROLE_PERMISSION)
		fun getRole(request: Request, roleData: RoleData): Response {
			if (roleData.updatePermission != null) {
				roleService.updateRolePermission(roleData.roleId, roleData.orgId, roleData.updatePermission)
			}

			val roleDto = roleService.getRole(roleData.roleId)

			val existingPermissions = roleDto.roleOrganizationPermissions
				.map { it.permission }
				.toSet()

			val permissionsView = Paginate(
				items = PermissionAccessEnum.entries.toList(),
				page = roleData.pageablePermission.page,
				size = 8
			)


			val roleDataButtons: List<List<Pair<String, CallbackData<Payload>>>> =
				permissionsView.currentPage().map { permission ->
					listOf(
						"${permission.nameRu}: ${if (existingPermissions.contains(permission)) "✅" else "❌"}" to
								CallbackData(
									CallbackQueryEnum.UPDATE_ROLE_PERMISSION,
									roleData.copy(updatePermission = permission)
								)
					)
				}

			val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

			navigationButtons.add(
				"Назад" to CallbackData(
					CallbackQueryEnum.PAGINATE_GET_ROLES,
					OrganizationPaginate(roleData.orgId)
				)
			)

			if (permissionsView.hasPrevious()) {
				navigationButtons.add(
					"⬅️ Назад" to CallbackData(
						CallbackQueryEnum.GET_ROLE, roleData.copy(
							pageablePermission = roleData.pageablePermission.decreasePage(), updatePermission = null
						)
					)
				)
			}
			if (permissionsView.hasNext()) {
				navigationButtons.add(
					"Вперёд ➡️" to CallbackData(
						CallbackQueryEnum.GET_ROLE, roleData.copy(
							pageablePermission = roleData.pageablePermission.increasePage(), updatePermission = null
						)
					)
				)
			}


			val buttons =
				telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + roleDataButtons)

			return Response(
				botApiMethods = listOf(
					EditMessageText().apply {
						messageId = UpdateUtil(request.update).getMessage()?.messageId ?: throw ValidationException()
						chatId = request.chatId.toString()
						text = "Выбрана роль ${roleDto.roleName}"
						replyMarkup = buttons
					}
				)
			)
		}

		@CallbackMapping(CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS)
		fun manageTablePermissions(request: Request, tableDataPaginate: TableDataPaginate): Response {

			val roles = organizationService.getRolesOrganization(
				tableDataPaginate.tableData.organizationId,
				page = tableDataPaginate.pageable.page
			)

			val roleDataButtons: List<List<Pair<String, CallbackData<Payload>>>> = roles.content.map { role ->
				listOf(
					role.roleName!! to CallbackData(
						CallbackQueryEnum.GET_TABLE_ROLE,
						role.id?.let {
							RoleTableData(
								tableDataPaginate.tableData.organizationId,
								it,
								tableDataPaginate.tableData.tableId
							)
						})
				)
			}

			val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

			navigationButtons.add("Назад" to CallbackData(CallbackQueryEnum.GET_TABLE, tableDataPaginate.tableData))

			navigationButtons.add(
				"Создать новую роль" to CallbackData(
					CallbackQueryEnum.CREATE_ROLE,
					OrganizationId(tableDataPaginate.tableData.organizationId)
				)
			)

			if (roles.hasPrevious()) {
				navigationButtons.add(
					"Вперёд ➡️" to CallbackData(
						CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS,
						tableDataPaginate.pageable.increasePage()
					)
				)
			}
			if (roles.hasNext()) {
				navigationButtons.add(
					"⬅️ Назад" to CallbackData(
						CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS,
						tableDataPaginate.pageable.decreasePage()
					)
				)
			}

			val buttons =
				telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + roleDataButtons)

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



	@CallbackMapping(CallbackQueryEnum.GET_TABLE_ROLE)
	fun getTableRole(request: Request, roleTableData: RoleTableData): Response {
		if (roleTableData.updateTablePermission != null) {
			roleService.updateRolePermissionByTableId(roleTableData.roleId, roleTableData.tableId, roleTableData.updateTablePermission)
		}

		val roleDto = roleService.getRole(roleTableData.roleId)

		val existingPermissions = roleDto.roleTablePermissions
			.map { it.permission }
			.toSet()

		val permissionsView = Paginate(
			items = TablePermissionAccessEnum.entries.toList(),
			page = roleTableData.pageable.page,
			size = 8
		)


		val roleDataButtons: List<List<Pair<String, CallbackData<Payload>>>> =
			permissionsView.currentPage().map { permission ->
				listOf(
					"${permission.nameRu}: ${if (existingPermissions.contains(permission)) "✅" else "❌"}" to
							CallbackData(
								CallbackQueryEnum.GET_TABLE_ROLE,
								roleTableData.copy(updateTablePermission = permission)
							)
				)
			}

		val navigationButtons = mutableListOf<Pair<String, CallbackData<Payload>>>()

		navigationButtons.add(
			"Назад" to CallbackData(
				CallbackQueryEnum.MANAGE_TABLE_PERMISSIONS,
				TableDataPaginate(TableData(roleTableData.organizationId, roleTableData.tableId))
			)
		)

		if (permissionsView.hasPrevious()) {
			navigationButtons.add(
				"⬅️ Назад" to CallbackData(
					CallbackQueryEnum.GET_TABLE_ROLE, roleTableData.copy(
						pageable = roleTableData.pageable.decreasePage(), updateTablePermission = null
					)
				)
			)
		}
		if (permissionsView.hasNext()) {
			navigationButtons.add(
				"Вперёд ➡️" to CallbackData(
					CallbackQueryEnum.GET_TABLE_ROLE, roleTableData.copy(
						pageable = roleTableData.pageable.increasePage(), updateTablePermission = null
					)
				)
			)
		}

		val buttons =
			telegramKeyboard.createInlineKeyboard("${request.userId}", listOf(navigationButtons) + roleDataButtons)

		return Response(
			botApiMethods = listOf(
				EditMessageText().apply {
					messageId = UpdateUtil(request.update).getMessage().messageId ?: throw ValidationException()
					chatId = request.chatId.toString()
					text = "Выбрана роль ${roleDto.roleName}"
					replyMarkup = buttons
				}
			)
		)
	}

}