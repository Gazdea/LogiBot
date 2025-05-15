package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationPaginate(
	val orgId: Long,
	val pageable: Pageable = Pageable(),
): Payload()
