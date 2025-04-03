package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.AuditLog}
 */
interface AuditLogInfo {
	val id: Long?
	val action: String?
	val timestamp: Instant?
	val user: UserInfo?
	val table: DataTableInfo?

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

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	interface DataTableInfo {
		val id: Long?
		val tableName: String?
		val createdAt: Instant?
	}
}
