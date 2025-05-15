package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.Instant

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