package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class UserOrganizationLinkMapper {

	abstract fun toEntity(userOrganizationLinkDto: UserOrganizationLinkDto): UserOrganizationLink

	abstract fun toDto(userOrganizationLink: UserOrganizationLink): UserOrganizationLinkDto



	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(
		userOrganizationLinkDto: UserOrganizationLinkDto,
		@MappingTarget userOrganizationLink: UserOrganizationLink
	): UserOrganizationLink
}