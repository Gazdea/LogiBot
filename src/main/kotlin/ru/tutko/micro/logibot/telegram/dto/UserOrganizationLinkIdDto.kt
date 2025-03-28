package ru.tutko.micro.logibot.telegram.dto

import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLinkId}
 */
data class UserOrganizationLinkIdDto(
    @field:NotNull var userId: Long? = null,
    @field:NotNull var organizationId: Long? = null
) : Serializable