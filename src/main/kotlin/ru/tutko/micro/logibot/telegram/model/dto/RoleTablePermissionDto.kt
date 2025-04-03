package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermission}
 */
data class RoleTablePermissionDto(
	var id: RoleTablePermissionIdDto? = null,
	var role: RoleDto? = null,
	var table: DataTableDto? = null,
	@field:NotNull @field:Size(max = 100) var permission: TablePermissionAccessEnum? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermissionId}
	 */
	data class RoleTablePermissionIdDto(
		@field:NotNull var roleId: Long? = null,
		@field:NotNull var tableId: Long? = null
	) : Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
	 */
	data class RoleDto(var id: Long? = null, @field:NotNull @field:Size(max = 50) var roleName: String? = null) :
		Serializable

	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	data class DataTableDto(
		var id: Long? = null,
		@field:NotNull @field:Size(max = 255) var tableName: String? = null,
		var createdAt: Instant? = null
	) : Serializable
}