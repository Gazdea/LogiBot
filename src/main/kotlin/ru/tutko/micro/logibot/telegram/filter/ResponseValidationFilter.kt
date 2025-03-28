package ru.tutko.micro.logibot.telegram.filter

import ru.tutko.micro.logibot.telegram.model.Response

interface ResponseValidationFilter {
	fun validate(response: Response):  Boolean

	fun process(chatIdUserId: String, response: Response)
}