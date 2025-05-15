package ru.tutko.micro.logibot.telegram.filter.response_impl

import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.filter.ResponseValidationFilter
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService

@Component
class WaitingForInputRemoveValidateFilter(
	private val redisService: CallbackRedisService
): ResponseValidationFilter {

	override fun validate(response: Response): Boolean {
		return response.clearWaitingForInput
	}

	override fun process(chatIdUserId: String, response: Response) {
		redisService.remove(chatIdUserId)
	}
}