package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermission}
 */
interface RoleTablePermissionInfo {
	val permission: String?
	val id: RoleTablePermissionIdInfo?
	val role: RoleInfo1?
	val table: DataTableInfo1?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermissionId}
	 */
	interface RoleTablePermissionIdInfo {
		val roleId: Long?
		val tableId: Long?
	}

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
	 */
	interface RoleInfo1 {
		val id: Long?
		val roleName: String?
	}

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.DataTable}
	 */
	interface DataTableInfo1 {
		val id: Long?
		val tableName: String?
		val createdAt: Instant?
	}
}
