package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable
import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum

@Serializable
data class TableDataColumnType(
	val tableData: TableData,
	val columnType: ColumnTypeEnum,
): Payload()
