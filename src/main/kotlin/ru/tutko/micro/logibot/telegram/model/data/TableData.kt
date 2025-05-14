package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class TableData(
	val organizationId: Long,
	val tableId: Long,
): Payload()
