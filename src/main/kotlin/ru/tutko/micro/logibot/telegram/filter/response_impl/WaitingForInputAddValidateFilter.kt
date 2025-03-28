package ru.tutko.micro.logibot.telegram.filter.response_impl

import ru.tutko.micro.logibot.telegram.component.TelegramSerialize
import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.component.WaitingForInputContextStorage
import ru.tutko.micro.logibot.telegram.filter.ResponseValidationFilter
import ru.tutko.micro.logibot.telegram.model.Response

@Component
class WaitingForInputAddValidateFilter(
	private val waitingForInputContextStorage: WaitingForInputContextStorage
): ResponseValidationFilter {

	override fun validate(response: Response): Boolean {
		return response.inputType != null
	}

	override fun process(chatIdUserId: String, response: Response) {
		waitingForInputContextStorage.set(chatIdUserId, TelegramSerialize.serializeData(response.inputType!!))
	}
}