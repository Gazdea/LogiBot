package ru.tutko.micro.logibot.telegram.model.dto.mongo

data class ColumnData(
	val columnId: Long,
	val name: String,
	val type: String,
	val value: String
)
