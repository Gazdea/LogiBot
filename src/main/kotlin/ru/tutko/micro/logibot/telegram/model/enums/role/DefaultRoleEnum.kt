package ru.tutko.micro.logibot.telegram.model.enums.role

import ru.tutko.micro.logibot.telegram.model.dto.RoleDto

enum class DefaultRoleEnum(val value: String, val permissionsAccessEnum: List<PermissionAccessEnum>?, val tablePermissionAccessEnum: List<TablePermissionAccessEnum>?) {
    ADMIN(
        "admin",
        listOf(
            PermissionAccessEnum.MANAGE_ROLES,
            PermissionAccessEnum.MANAGE_EMPLOYEE_ROLES,
            PermissionAccessEnum.CREATE_REPORT,
            PermissionAccessEnum.MANAGE_EMPLOYEES,
            PermissionAccessEnum.VIEW_LOGS,
            PermissionAccessEnum.VIEW_EMPLOYEES,
            PermissionAccessEnum.MANAGE_TABLES
        ),
        TablePermissionAccessEnum.entries.toList()
    ),

    MODERATOR(
        "moderator",
        listOf(
            PermissionAccessEnum.VIEW_EMPLOYEES,
            PermissionAccessEnum.VIEW_LOGS,
            PermissionAccessEnum.CREATE_REPORT,
        ),
        listOf(
            TablePermissionAccessEnum.CREATE_REPORT,
            TablePermissionAccessEnum.VIEW_TABLE,
            TablePermissionAccessEnum.FILL_TABLE,
            TablePermissionAccessEnum.EXPORT_TABLE
        )
    ),

    USER("user",
        listOf(
        PermissionAccessEnum.VIEW_EMPLOYEES
        ),
        listOf()
    );

    companion object {
        fun getRolesDto(organizationId: Long): List<RoleDto> = entries.map { role ->
            RoleDto(
                roleName = role.value,
                organization = RoleDto.OrganizationDto(id = organizationId),
                roleOrganizationPermissions = role.permissionsAccessEnum?.map {permissionAccessEnum ->
                    RoleDto.RoleOrganizationPermissionDto(permission = permissionAccessEnum)
                }?.toMutableSet() ?: mutableSetOf(),

//                roleTablePermissions = role.tablePermissionAccessEnum?.map { tablePermissionAccessEnum ->
//                    RoleDto.RoleTablePermissionDto(permission = tablePermissionAccessEnum.value)
//                }?.toMutableSet() ?: mutableSetOf()
            )
        }

    }
}
