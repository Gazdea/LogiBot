package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import ru.tutko.micro.logibot.telegram.mapper.ChatMapper
import ru.tutko.micro.logibot.telegram.dto.ChatDto
import ru.tutko.micro.logibot.telegram.dto.request.FindOrMigrateChatDtoRequest
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

    override fun getChatsByOrganizationId(organizationId: Long): MutableList<ChatDto> {
        return chatRepository.findByOrganization_Id(organizationId).map { chatMapper.toDto(it) }.toMutableList()
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

    override fun findOrMigrateChat(chatMigrateDto: FindOrMigrateChatDtoRequest): Optional<ChatDto> {
        val existingChat = chatRepository.findByChatId(chatMigrateDto.chatId!!).orElse(null)
        val oldChat = chatMigrateDto.fromChatId?.let { chatRepository.findByChatId(it).orElse(null) }

        return when {
            existingChat != null -> Optional.of(chatMapper.toDto(existingChat))

            oldChat != null && chatMigrateDto.toChatId != null -> {
                oldChat.chatId = chatMigrateDto.toChatId
                Optional.of(chatMapper.toDto(chatRepository.save(oldChat)))
            }

            else -> Optional.of(chatMapper.toDto(chatRepository.save(chatMapper.migrateDtoToEntity(chatMigrateDto))))
        }
    }

    override fun exists(chatId: Long): Boolean {
        return chatRepository.existsByChatId(chatId)
    }

}