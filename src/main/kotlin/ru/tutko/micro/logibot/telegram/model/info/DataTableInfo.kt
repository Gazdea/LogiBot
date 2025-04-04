package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
 */
interface DataTableInfo {
	val id: Long?
	val tableName: String?
	val createdAt: Instant?
	val organization: OrganizationInfo?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	interface OrganizationInfo {
		val id: Long?
		val name: String?
	}
}
