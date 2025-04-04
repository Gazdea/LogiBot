package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.Serializable

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission}
 */
data class RoleOrganizationPermissionDto(
	var id: RoleOrganizationPermissionIdDto? = null,
	var organization: OrganizationDto1? = null,
	var role: RoleDto? = null,
	@field:NotNull @field:Size(max = 100) var permission: String? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermissionId}
	 */
	data class RoleOrganizationPermissionIdDto(
		@field:NotNull var organizationId: Long? = null,
		@field:NotNull var roleId: Long? = null
	) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	data class OrganizationDto1(var id: Long? = null, @field:Size(max = 255) var name: String? = null) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
	 */
	data class RoleDto(var id: Long? = null, @field:NotNull @field:Size(max = 50) var roleName: String? = null) :
		Serializable
}