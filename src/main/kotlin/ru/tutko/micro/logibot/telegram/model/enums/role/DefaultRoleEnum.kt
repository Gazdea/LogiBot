package ru.tutko.micro.logibot.telegram.model.enums.role

import ru.tutko.micro.logibot.telegram.model.dto.RoleDto

enum class DefaultRoleEnum(val value: String, val permissionsAccessEnum: List<PermissionAccessEnum>?, val tablePermissionAccessEnum: List<TablePermissionAccessEnum>?) {
    ADMIN(
        "admin",
        listOf(
            PermissionAccessEnum.CREATOR
        ),
        listOf(
            TablePermissionAccessEnum.CREATOR
        )
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
            )
        }

    }
}
