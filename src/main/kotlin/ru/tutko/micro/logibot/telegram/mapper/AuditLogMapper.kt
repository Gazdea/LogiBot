package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.AuditLogDto
import ru.tutko.micro.logibot.telegram.model.entity.AuditLog

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class AuditLogMapper {

	abstract fun toEntity(auditLogDto: AuditLogDto): AuditLog

	abstract fun toDto(auditLog: AuditLog): AuditLogDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(auditLogDto: AuditLogDto, @MappingTarget auditLog: AuditLog): AuditLog
}