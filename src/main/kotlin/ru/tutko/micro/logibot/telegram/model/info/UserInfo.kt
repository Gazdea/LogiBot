package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

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
