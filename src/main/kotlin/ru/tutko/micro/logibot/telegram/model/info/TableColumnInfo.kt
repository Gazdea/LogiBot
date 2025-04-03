package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.TableColumn}
 */
interface TableColumnInfo {
	val id: Long?
	val columnName: String?
	val columnType: String?
	val table: DataTableInfo?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	interface DataTableInfo {
		val id: Long?
		val tableName: String?
		val createdAt: Instant?
	}
}
