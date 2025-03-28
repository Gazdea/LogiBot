package ru.tutko.micro.logibot.telegram.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Chat}
 */
data class ChatDto(
	var id: Long? = null,
	var chatId: Long? = null,
	@field:NotNull var organization: OrganizationDto? = null,
	@field:Size(max = 50) var type: String? = null,
	@field:Size(max = 255) var title: String? = null,
	@field:Size(max = 100) var username: String? = null,
	var createdAt: Instant? = null
) : Serializable