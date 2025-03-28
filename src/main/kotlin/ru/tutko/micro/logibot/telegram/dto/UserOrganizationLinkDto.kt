package ru.tutko.micro.logibot.telegram.dto

import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink}
 */
data class UserOrganizationLinkDto(
	var id: UserOrganizationLinkIdDto? = null,
	var user: UserDto? = null,
	var organization: OrganizationDto? = null,
	var roleId: Long? = null,
	var joinedAt: Instant? = null
) : Serializable