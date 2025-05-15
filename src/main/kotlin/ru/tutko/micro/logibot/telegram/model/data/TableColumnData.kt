package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class TableColumnData(
	val organizationId: Long,
	val tableId: Long,
	val columnId: Long
): Payload()