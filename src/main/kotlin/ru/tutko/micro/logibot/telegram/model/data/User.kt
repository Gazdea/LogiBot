package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
	val userId: Long,
): Payload()
