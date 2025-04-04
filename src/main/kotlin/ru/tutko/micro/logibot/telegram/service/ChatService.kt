package ru.tutko.micro.logibot.telegram.service

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import ru.tutko.micro.logibot.telegram.model.dto.ChatDto
import ru.tutko.micro.logibot.telegram.model.dto.request.FindOrMigrateChatDtoRequest

import java.util.*

interface ChatService {
    fun getChats(): MutableList<ChatDto>

    fun getChatById(id: Long): Optional<ChatDto>

    fun getChatsByOrganizationId(organizationId: Long): List<ChatDto>

    fun createChat(chat: ChatDto): ChatDto

    fun updateChat(chat: ChatDto): ChatDto

    fun deleteChat(chatId: Long)

    fun findOrMigrateChat(chatMigrateDto: FindOrMigrateChatDtoRequest): Optional<ChatDto>

    fun exists(chatId: Long): Boolean
}