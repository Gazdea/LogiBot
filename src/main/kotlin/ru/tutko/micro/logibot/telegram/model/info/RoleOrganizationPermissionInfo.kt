package ru.tutko.micro.logibot.telegram.model.info

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission}
 */
interface RoleOrganizationPermissionInfo {
	val permission: String?
	val organization: OrganizationInfo?
	val role: RoleInfo?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	interface OrganizationInfo {
		val id: Long?
		val name: String?
	}

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
	 */
	interface RoleInfo {
		val id: Long?
		val roleName: String?
	}
}
