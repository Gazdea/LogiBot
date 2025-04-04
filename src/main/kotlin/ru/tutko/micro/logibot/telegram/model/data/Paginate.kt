package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable

@Serializable
data class Paginate (
	val page: Int
)
{
	fun increasePage(): Int {
		return  this.page.inc()
	}

	fun decreasePage(): Int {
		return this.page.dec()
	}
}