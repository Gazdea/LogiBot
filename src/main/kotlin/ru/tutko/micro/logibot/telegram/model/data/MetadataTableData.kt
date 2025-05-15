package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class MetadataTableData(
	val organizationId: Long,
	val tableId: Long,
	val dataId: Long,
): Payload()
