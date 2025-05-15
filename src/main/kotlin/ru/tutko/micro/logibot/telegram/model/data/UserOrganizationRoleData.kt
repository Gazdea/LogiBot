package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class UserOrganizationRoleData(
	val userId: Long,
	val organizationId: Long,
	val roleId: Long
): Payload()
