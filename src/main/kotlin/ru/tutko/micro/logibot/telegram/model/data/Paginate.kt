package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Paginate(val page: Int) : Payload() {
	fun increasePage(): Paginate = copy(page = page + 1)
	fun decreasePage(): Paginate = copy(page = page - 1)
}