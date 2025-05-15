package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class UserOrganizationPaginate(
	val userOrganizationData: UserOrganizationData,
	val pageable: Pageable
): Payload()
