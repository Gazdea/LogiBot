package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.Instant
import java.util.*

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.TableDataMetadatum}
 */
data class TableDataMetadatumDto(
	var id: Long? = null,
	@field:NotNull var table: DataTableDto? = null,
	@field:NotNull var mongoDocumentId: UUID? = null,
	@field:NotNull var user: UserDto? = null,
	var createdAt: Instant? = null,
	@field:Size(max = 50) var action: String? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	data class DataTableDto(
		var id: Long? = null,
		@field:NotNull @field:Size(max = 255) var tableName: String? = null,
		var createdAt: Instant? = null
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
}