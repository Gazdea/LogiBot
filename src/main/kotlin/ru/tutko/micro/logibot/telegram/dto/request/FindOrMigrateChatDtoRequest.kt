package ru.tutko.micro.logibot.telegram.dto.request

import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import java.io.Serializable
import java.time.Instant

data class FindOrMigrateChatDtoRequest(
    var id: Long? = null,
    var chatId: Long? = null,
    var type: ChatTypeEnum? = null,
    @field:Size(max = 255) var title: String? = null,
    @field:Size(max = 100) var username: String? = null,
    var fromChatId: Long? = null,
    var toChatId: Long? = null,
    var createdAt: Instant? = null
) : Serializable