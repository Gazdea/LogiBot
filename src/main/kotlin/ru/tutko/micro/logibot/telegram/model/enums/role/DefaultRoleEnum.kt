package ru.tutko.micro.logibot.telegram.model.enums.role

import ru.tutko.micro.logibot.telegram.dto.RoleDto
import ru.tutko.micro.logibot.telegram.dto.RolePermissionDto

enum class DefaultRoleEnum(val value: String, val permissionsAccessEnum: List<PermissionAccessEnum>?) {
    ADMIN(
        "admin", listOf(
            PermissionAccessEnum.MANAGE_ROLES,
            PermissionAccessEnum.MANAGE_EMPLOYEE_ROLES,
            PermissionAccessEnum.CREATE_REPORT,
            PermissionAccessEnum.MANAGE_EMPLOYEES,
            PermissionAccessEnum.VIEW_LOGS,
            PermissionAccessEnum.VIEW_EMPLOYEES,
            PermissionAccessEnum.MANAGE_TABLES
        )
    ),

    MODERATOR(
        "moderator", listOf(
            PermissionAccessEnum.VIEW_EMPLOYEES,
            PermissionAccessEnum.VIEW_LOGS,
            PermissionAccessEnum.CREATE_REPORT,
        )
    ),

    USER("user", listOf(
        PermissionAccessEnum.VIEW_EMPLOYEES
    ));

    companion object {
        fun getRolesDto(organizationId: Long): List<RoleDto> = entries.map { role ->
            RoleDto(
                role = role.value,
                organizationId = organizationId,
                rolePermissions = (role.permissionsAccessEnum?.map { permissionAccessEnum ->
                    RolePermissionDto(permission = permissionAccessEnum.value)
                }?.toMutableSet()) ?: mutableSetOf()
            )
        }
    }
}
