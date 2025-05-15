package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class UserOrganizationData(
	val userId: Long,
	val organizationId: Long
): Payload()
