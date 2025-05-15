package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class ColumnTableSaveMetaData(
	val columnId: Long,
	val tableSaveMetaData: TableSaveMetaData,
) : Payload()