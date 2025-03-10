package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.Size
import java.io.Serializable

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
 */
data class OrganizationDto(
    var id: Long? = null,
    @field:Size(max = 255) var name: String? = null
) : Serializable