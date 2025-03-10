package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.entity.Organization

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
abstract class OrganizationMapper {

    @Mapping(source = "name", target = "name")
    abstract fun toEntity(organizationDto: OrganizationDto): Organization

    @Mapping(source = "name", target = "name")
    abstract fun toDto(organization: Organization): OrganizationDto

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    abstract fun partialUpdate(organizationDto: OrganizationDto, @MappingTarget organization: Organization)
}
