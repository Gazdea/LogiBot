package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink}
 */
data class UserOrganizationLinkDto(
	var id: UserOrganizationLinkIdDto? = null,
	var user: UserDto? = null,
	var organization: OrganizationDto1? = null,
	var role: RoleDto? = null,
	var joinedAt: Instant? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLinkId}
	 */
	data class UserOrganizationLinkIdDto(
		@field:NotNull var userId: Long? = null,
		@field:NotNull var organizationId: Long? = null,
		@field:NotNull var roleId: Long? = null
	) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.User}
	 */
	data class UserDto(
		var id: Long? = null,
		var externalUserId: Long? = null,
		@field:Size(max = 100) var username: String? = null,
		@field:Size(max = 100) var firstName: String? = null,
		@field:Size(max = 100) var lastName: String? = null,
		var createdAt: Instant? = null
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