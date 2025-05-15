package ru.tutko.micro.logibot.telegram.model.dto.mongo

import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "table_metadata")
data class TableMetadata(
	val id: UUID = UUID.randomUUID(),
	val tableName: String,
	val columns: List<ColumnData>
)