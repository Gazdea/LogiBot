package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.mapper.ChatMapper
import ru.tutko.micro.logibot.telegram.model.dto.ChatDto
import ru.tutko.micro.logibot.telegram.model.dto.request.FindOrMigrateChatDtoRequest
import ru.tutko.micro.logibot.telegram.repository.ChatRepository
import ru.tutko.micro.logibot.telegram.service.ChatService
import java.util.*

@Service
class ChatServiceImpl(
    private val chatRepository: ChatRepository,
    private val chatMapper: ChatMapper
): ChatService {
    override fun getChats(): MutableList<ChatDto> {
        return chatRepository.findAll().map { chatMapper.toDto(it) }.toMutableList()
    }

    override fun getChatById(id: Long): Optional<ChatDto> {
        return chatRepository.findById(id).map { chatMapper.toDto(it) }
    }

    override fun getChatsByOrganizationId(organizationId: Long): List<ChatDto> {
        return chatRepository.findByOrganization_Id(organizationId).map { chatMapper.toDto(it) }
    }

    override fun createChat(chat: ChatDto): ChatDto {
        return chatMapper.toDto(chatRepository.save(chatMapper.toEntity(chat)))
    }

    override fun updateChat(chat: ChatDto): ChatDto {
        return chatMapper.toDto(chatRepository.save(chatMapper.toEntity(chat)))
    }

    override fun deleteChat(chatId: Long) {
        chatRepository.deleteById(chatId)
    }

    @Transactional
    override fun findOrMigrateChat(chatMigrateDto: FindOrMigrateChatDtoRequest): Optional<ChatDto> {
        val existingChat = chatRepository.findByExternalChatId(chatMigrateDto.chatId!!).orElse(null)
        val oldChat = chatMigrateDto.fromChatId?.let { chatRepository.findByExternalChatId(it).orElse(null) }

        return when {
            existingChat != null -> Optional.of(chatMapper.toDto(existingChat))

            oldChat != null && chatMigrateDto.toChatId != null -> {
                oldChat.externalChatId = chatMigrateDto.toChatId
                Optional.of(chatMapper.toDto(chatRepository.save(oldChat)))
            }

            else -> Optional.empty()
        }
    }

    override fun exists(chatId: Long): Boolean {
        return chatRepository.existsByExternalChatId(chatId)
    }

}