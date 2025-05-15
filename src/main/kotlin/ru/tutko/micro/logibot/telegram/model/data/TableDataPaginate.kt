package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class TableDataPaginate(
	val tableData: TableData,
	val pageable: Pageable = Pageable()
): Payload()
