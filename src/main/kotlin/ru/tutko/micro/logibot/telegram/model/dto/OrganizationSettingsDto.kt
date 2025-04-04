package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.OrganizationSettingsEnum
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.OrganizationSettings}
 */
data class OrganizationSettingsDto(
	var id: Long? = null,
	var organization: OrganizationDto? = null,
	@field:Size(max = 100) var setting_key: OrganizationSettingsEnum? = null,
	var setting_value: String? = null,
	var created_at: Instant? = null,
	var updated_at: Instant? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	data class OrganizationDto(var id: Long? = null, @field:Size(max = 255) var name: String? = null) : Serializable
}