package ru.tutko.micro.logibot.telegram.dispatcher

import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.filter.ResponseValidationFilter
import ru.tutko.micro.logibot.telegram.model.Response

@Component
class ResponseProcessor(private val filters: List<ResponseValidationFilter>) {
	fun process(response: Response, key: String) {
		filters.forEach { filter ->
			if (filter.validate(response)) filter.process(key, response)
		}
	}
}