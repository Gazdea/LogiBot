package ru.tutko.micro.logibot.telegram.dispatcher

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Component
class RequestFactory(private val redisService: CallbackRedisService) {
	fun create(update: Update): Request {
		val chatId = UpdateUtil(update).getChat().id
		val userId = UpdateUtil(update).getUser().id
		val handlerType = UpdateUtil(update).getHandlerType()

		val chatIdUserId = "$chatId:$userId"
		val callback = when (handlerType) {
			HandlerTypeEnum.INPUT -> redisService.get(chatIdUserId)
			HandlerTypeEnum.CALLBACK -> redisService.get(update.callbackQuery.data)
			else -> null
		}

		return Request(chatId, userId, update, handlerType, callback)
	}
}
