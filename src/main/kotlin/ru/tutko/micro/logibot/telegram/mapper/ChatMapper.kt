package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.ChatDto
import ru.tutko.micro.logibot.telegram.model.dto.request.FindOrMigrateChatDtoRequest
import ru.tutko.micro.logibot.telegram.model.entity.Chat

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class ChatMapper {

	abstract fun toEntity(chatDto: ChatDto): Chat

	abstract fun toDto(chat: Chat): ChatDto

	abstract fun migrateDtoToEntity(findOrMigrateChatDtoRequest: FindOrMigrateChatDtoRequest): Chat

	@Condition
	fun isNotEmpty(value: String?): Boolean {
		return !value.isNullOrEmpty()
	}

	@Condition
	fun isNotEmptyCollection(value: Collection<*>?): Boolean {
		return !value.isNullOrEmpty()
	}

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
	abstract fun partialUpdate(chatDto: ChatDto, @MappingTarget chat: Chat): Chat
}