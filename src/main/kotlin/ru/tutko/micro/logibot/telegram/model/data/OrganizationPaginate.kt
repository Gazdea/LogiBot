package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationPaginate(
	val organizationId: Long,
	val paginate: Paginate
)
