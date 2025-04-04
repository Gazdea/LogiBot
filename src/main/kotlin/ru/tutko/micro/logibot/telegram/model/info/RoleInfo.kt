package ru.tutko.micro.logibot.telegram.model.info

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Role}
 */
interface RoleInfo {
	val id: Long?
	val roleName: String?
	val organization: OrganizationInfo?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	interface OrganizationInfo {
		val id: Long?
		val name: String?
	}
}
