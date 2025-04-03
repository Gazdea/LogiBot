package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.RoleOrganizationPermissionDto
import ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class RoleOrganizationPermissionMapper {

	abstract fun toEntity(roleOrganizationPermissionDto: RoleOrganizationPermissionDto): RoleOrganizationPermission

	abstract fun toDto(roleOrganizationPermission: RoleOrganizationPermission): RoleOrganizationPermissionDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(
		roleOrganizationPermissionDto: RoleOrganizationPermissionDto,
		@MappingTarget roleOrganizationPermission: RoleOrganizationPermission
	): RoleOrganizationPermission
}