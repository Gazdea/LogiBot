package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.entity.Role

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
	uses = [OrganizationMapper::class]
)
abstract class RoleMapper {

	abstract fun toEntity(roleDto: RoleDto): Role

	abstract fun toDto(role: Role): RoleDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(roleDto: RoleDto, @MappingTarget role: Role): Role
}