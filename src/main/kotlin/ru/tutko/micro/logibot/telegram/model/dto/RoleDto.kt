package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum
import java.io.Serializable

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
 */
data class RoleDto(
	var id: Long? = null,
	@field:NotNull var organization: OrganizationDto? = null,
	@field:NotNull @field:Size(max = 50) var roleName: String? = null,
	var roleOrganizationPermissions: MutableSet<RoleOrganizationPermissionDto> = mutableSetOf(),
	var roleTablePermissions: MutableSet<RoleTablePermissionDto> = mutableSetOf()
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	data class OrganizationDto(var id: Long? = null, @field:Size(max = 255) var name: String? = null) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission}
	 */
	data class RoleOrganizationPermissionDto(
		@field:NotNull @field:Size(max = 100) var permission: PermissionAccessEnum? = null,
	) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermission}
	 */
	data class RoleTablePermissionDto(@field:NotNull @field:Size(max = 100) var permission: TablePermissionAccessEnum? = null) :
		Serializable
}