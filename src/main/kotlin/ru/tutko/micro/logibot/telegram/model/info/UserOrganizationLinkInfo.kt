package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink}
 */
interface UserOrganizationLinkInfo {
	val joinedAt: Instant?
	val id: UserOrganizationLinkIdInfo?
	val user: UserInfo1?
	val organization: OrganizationInfo?
	val role: RoleInfo?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLinkId}
	 */
	interface UserOrganizationLinkIdInfo {
		val userId: Long?
		val organizationId: Long?
		val roleId: Long?
	}

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.User}
	 */
	interface UserInfo1 {
		val id: Long?
		val externalUserId: Long?
		val username: String?
		val firstName: String?
		val lastName: String?
		val createdAt: Instant?
	}

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
