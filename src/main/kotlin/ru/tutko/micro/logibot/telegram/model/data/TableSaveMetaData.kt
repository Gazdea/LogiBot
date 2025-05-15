package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class TableSaveMetaData(
	val tableData: TableData,
	val filledTableRow: MutableMap<Long, String> = mutableMapOf(),
	val pageable: Pageable = Pageable(),
): Payload()
