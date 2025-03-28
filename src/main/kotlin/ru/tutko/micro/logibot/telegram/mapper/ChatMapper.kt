package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.dto.ChatDto
import ru.tutko.micro.logibot.telegram.dto.request.FindOrMigrateChatDtoRequest
import ru.tutko.micro.logibot.telegram.model.entity.Chat

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class ChatMapper {

    abstract fun toEntity(chatDto: ChatDto): Chat

    abstract fun toDto(chat: Chat): ChatDto

    abstract fun migrateDtoToEntity(chatMigrateDto: FindOrMigrateChatDtoRequest): Chat

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(chatDto: ChatDto, @MappingTarget chat: Chat): Chat
}