package ru.tutko.micro.logibot.telegram.mapper

import org.mapstruct.*
import ru.tutko.micro.logibot.telegram.model.dto.TableDataMetadatumDto
import ru.tutko.micro.logibot.telegram.model.entity.TableDataMetadatum

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
	uses = [DataTableMapper::class]
)
abstract class TableDataMetadatumMapper {

	abstract fun toEntity(tableDataMetadatumDto: TableDataMetadatumDto): TableDataMetadatum

	abstract fun toDto(tableDataMetadatum: TableDataMetadatum): TableDataMetadatumDto

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	abstract fun partialUpdate(
		tableDataMetadatumDto: TableDataMetadatumDto,
		@MappingTarget tableDataMetadatum: TableDataMetadatum
	): TableDataMetadatum
}