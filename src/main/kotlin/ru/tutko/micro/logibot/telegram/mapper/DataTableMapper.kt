package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.DataTableDto
import ru.tutko.micro.logibot.telegram.model.entity.DataTable

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
abstract class DataTableMapper {

	abstract fun toEntity(dataTableDto: DataTableDto): DataTable

	abstract fun toDto(dataTable: DataTable): DataTableDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(dataTableDto: DataTableDto, @MappingTarget dataTable: DataTable): DataTable
}