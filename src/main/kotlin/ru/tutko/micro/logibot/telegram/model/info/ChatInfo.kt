package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Chat}
 */
interface ChatInfo {
	val id: Long?
	val externalChatId: Long?
	val type: String?
	val title: String?
	val chatUsername: String?
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
