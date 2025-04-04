package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.TableColumnDto
import ru.tutko.micro.logibot.telegram.model.entity.TableColumn

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
	uses = [DataTableMapper::class]
)
abstract class TableColumnMapper {

	abstract fun toEntity(tableColumnDto: TableColumnDto): TableColumn

	abstract fun toDto(tableColumn: TableColumn): TableColumnDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(tableColumnDto: TableColumnDto, @MappingTarget tableColumn: TableColumn): TableColumn
}