package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Pageable(val page: Int = 0) : Payload() {
	fun increasePage(): Pageable = copy(page = page + 1)
	fun decreasePage(): Pageable = copy(page = maxOf(0, page - 1))
}
