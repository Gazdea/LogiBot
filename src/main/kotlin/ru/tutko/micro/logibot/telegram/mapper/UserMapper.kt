package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.dto.UserDto
import ru.tutko.micro.logibot.telegram.model.entity.User

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class UserMapper {

    abstract fun toEntity(userDto: UserDto): User

    abstract fun toDto(user: User): UserDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(userDto: UserDto, @MappingTarget user: User): User
}