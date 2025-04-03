package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant
import java.util.*

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.TableDataMetadatum}
 */
interface TableDataMetadatumInfo {
	val id: Long?
	val mongoDocumentId: UUID?
	val createdAt: Instant?
	val action: String?
	val table: DataTableInfo?
	val user: UserInfo?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	interface DataTableInfo {
		val id: Long?
		val tableName: String?
		val createdAt: Instant?
	}

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.User}
	 */
	interface UserInfo {
		val id: Long?
		val externalUserId: Long?
		val username: String?
		val firstName: String?
		val lastName: String?
		val createdAt: Instant?
	}
}
