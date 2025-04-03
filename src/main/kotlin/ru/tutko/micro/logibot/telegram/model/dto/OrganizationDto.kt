package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
 */
data class OrganizationDto(
	var id: Long? = null,
	@field:Size(max = 255) var name: String? = null,
	var chats: MutableSet<ChatDto> = mutableSetOf(),
	var dataTables: MutableSet<DataTableDto> = mutableSetOf(),
	var roles: MutableSet<RoleDto> = mutableSetOf(),
	var roleOrganizationPermissions: MutableSet<RoleOrganizationPermissionDto> = mutableSetOf(),
	var userOrganizationLinks: MutableSet<UserOrganizationLinkDto> = mutableSetOf()
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Chat}
	 */
	data class ChatDto(
		var id: Long? = null,
		var externalChatId: Long? = null,
		@field:Size(max = 50) var type: ChatTypeEnum? = null,
		@field:Size(max = 255) var title: String? = null,
		@field:Size(max = 100) var chatUsername: String? = null,
		var createdAt: Instant? = null
	) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	data class DataTableDto(
		var id: Long? = null,
		@field:NotNull @field:Size(max = 255) var tableName: String? = null,
		var createdAt: Instant? = null
	) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
	 */
	data class RoleDto(var id: Long? = null, @field:NotNull @field:Size(max = 50) var roleName: String? = null) :
		Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission}
	 */
	data class RoleOrganizationPermissionDto(@field:NotNull @field:Size(max = 100) var permission: PermissionAccessEnum? = null) :
		Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink}
	 */
	data class UserOrganizationLinkDto(var joinedAt: Instant? = null) : Serializable
}