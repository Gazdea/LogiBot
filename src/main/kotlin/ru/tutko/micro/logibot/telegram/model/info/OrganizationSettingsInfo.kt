package ru.tutko.micro.logibot.telegram.model.info

import java.time.Instant

/**
 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.OrganizationSettings}
 */
interface OrganizationSettingsInfo {
	val id: Long?
	val setting_key: String?
	val setting_value: String?
	val created_at: Instant?
	val updated_at: Instant?
	val organization: OrganizationInfo1?

	/**
	 * Projection for {@link ru.tutko.micro.logibot.telegram.model.entity.Organization}
	 */
	interface OrganizationInfo1 {
		val id: Long?
		val name: String?
	}
}
