package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
 */
data class DataTableDto(
	var id: Long? = null,
	@field:NotNull var organization: OrganizationDto? = null,
	@field:NotNull @field:Size(max = 255) var tableName: String? = null,
	var createdAt: Instant? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	data class OrganizationDto(var id: Long? = null, @field:Size(max = 255) var name: String? = null) : Serializable
}