package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.entity.Organization

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
	uses = [ChatMapper::class, DataTableMapper::class, RoleMapper::class, RoleOrganizationPermissionMapper::class, UserOrganizationLinkMapper::class]
)
abstract class OrganizationMapper {

	abstract fun toEntity(organizationDto: OrganizationDto): Organization

	abstract fun toDto(organization: Organization): OrganizationDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(
		organizationDto: OrganizationDto,
		@MappingTarget organization: Organization
	): Organization
}