package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.AuditLog}
 */
data class AuditLogDto(
	var id: Long? = null,
	var user: UserDto? = null,
	var table: DataTableDto? = null,
	@field:Size(max = 50) var action: String? = null,
	var timestamp: Instant? = null
) : Serializable {
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
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	data class DataTableDto(
		var id: Long? = null,
		@field:NotNull @field:Size(max = 255) var tableName: String? = null,
		var createdAt: Instant? = null
	) : Serializable
}