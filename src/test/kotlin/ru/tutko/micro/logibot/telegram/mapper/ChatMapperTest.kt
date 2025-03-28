package ru.tutko.micro.logibot.telegram.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.tutko.micro.logibot.telegram.dto.ChatDto
import ru.tutko.micro.logibot.telegram.model.entity.Chat
import ru.tutko.micro.logibot.telegram.utils.DtoTestBuilder
import ru.tutko.micro.logibot.telegram.utils.EntityTestBuilder

class ChatMapperTest {
	private val chatMapper = ChatMapperImpl()
	private val entityTestBuilder = EntityTestBuilder()
	private val dtoTestBuilder = DtoTestBuilder()

	private var chat = Chat()
	private var chatDto = ChatDto()

	@BeforeEach
	fun setUp() {
		chat = entityTestBuilder.getChat()
		chatDto = dtoTestBuilder.getChatDto()
	}

	@Test
	fun toEntity() {
		val mappedChat = chatMapper.toEntity(chatDto)

		assertAll({
			assertEquals(chat.id, mappedChat.id)
			assertEquals(chat.chatId, mappedChat.chatId)
			assertEquals(chat.username, mappedChat.username)
			assertEquals(chat.title, mappedChat.title)
			assertEquals(chat.type, mappedChat.type)
			assertEquals(chat.createdAt, mappedChat.createdAt)
		}
		)
	}

	@Test
	fun toDto() {
		val mappedChat = chatMapper.toDto(chat)

		assertAll({
			assertEquals(chatDto.id, mappedChat.id)
			assertEquals(chatDto.chatId, mappedChat.chatId)
			assertEquals(chatDto.username, mappedChat.username)
			assertEquals(chatDto.title, mappedChat.title)
			assertEquals(chatDto.type, mappedChat.type)
			assertEquals(chatDto.createdAt, mappedChat.createdAt)
		})
	}

	@Test
	fun migrateDtoToEntity() {
	}
}