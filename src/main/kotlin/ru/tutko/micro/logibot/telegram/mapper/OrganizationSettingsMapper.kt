package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationSettingsDto
import ru.tutko.micro.logibot.telegram.model.entity.OrganizationSettings

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
	uses = [OrganizationMapper::class]
)
abstract class OrganizationSettingsMapper {

	abstract fun toEntity(organizationSettingsDto: OrganizationSettingsDto): OrganizationSettings

	abstract fun toDto(organizationSettings: OrganizationSettings): OrganizationSettingsDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(
		organizationSettingsDto: OrganizationSettingsDto,
		@MappingTarget organizationSettings: OrganizationSettings
	): OrganizationSettings
}