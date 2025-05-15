package ru.tutko.micro.logibot.telegram.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum
import java.io.Serializable
import java.time.Instant

/**
 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.TableColumn}
 */
data class TableColumnDto(
	var id: Long? = null,
	@field:NotNull var table: DataTableDto? = null,
	@field:NotNull @field:Size(max = 255) var columnName: String? = null,
	@field:NotNull @field:Size(max = 50) var columnType: ColumnTypeEnum? = null
) : Serializable {
	/**
	 * DTO for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	data class DataTableDto(
		var id: Long? = null,
		@field:NotNull @field:Size(max = 255) var tableName: String? = null,
		var createdAt: Instant? = null
	) : Serializable
}