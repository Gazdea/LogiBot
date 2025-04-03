package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Chat}
 */
data class ChatDto(
	var id: Long? = null,
	var externalChatId: Long? = null,
	@field:NotNull var organization: OrganizationDto? = null,
	@field:Size(max = 50) var type: ChatTypeEnum? = null,
	@field:Size(max = 255) var title: String? = null,
	@field:Size(max = 100) var chatUsername: String? = null,
	var createdAt: Instant? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	data class OrganizationDto(var id: Long? = null, @field:Size(max = 255) var name: String? = null) : Serializable
}