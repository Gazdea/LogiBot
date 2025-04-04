package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.RoleTablePermissionDto
import ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermission

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
	uses = [RoleMapper::class]
)
abstract class RoleTablePermissionMapper {

	abstract fun toEntity(roleTablePermissionDto: RoleTablePermissionDto): RoleTablePermission

	abstract fun toDto(roleTablePermission: RoleTablePermission): RoleTablePermissionDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(
		roleTablePermissionDto: RoleTablePermissionDto,
		@MappingTarget roleTablePermission: RoleTablePermission
	): RoleTablePermission
}