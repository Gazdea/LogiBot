package ru.tutko.micro.logibot.telegram.model.table

import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum

data class FilledCell(
	val cellName: String,
	val fieldType: ColumnTypeEnum,
	val value: Any?
)
