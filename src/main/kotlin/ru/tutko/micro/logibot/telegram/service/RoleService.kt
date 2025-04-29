package ru.tutko.micro.logibot.telegram.service

import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
interface RoleService {

	fun getRole(roleId: Long): RoleDto

	fun getRoleByUserOrganization(organizationId: Long, externalUserId: Long): RoleDto

	fun userExistsPermission(organizationId: Long, externalUserId: Long, permission: PermissionAccessEnum): Boolean

	fun createRole(organizationId: Long, roleName: String): RoleDto

	fun getRolesByOrganizationId(organizationId: Long): List<RoleDto>
}