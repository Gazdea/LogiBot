package ru.tutko.micro.logibot.telegram.dispatcher

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.tutko.micro.logibot.telegram.exception.*

@Component
class ErrorHandler {

	private val logger = LoggerFactory.getLogger(UpdateDispatcher::class.java)

	fun handle(e: Exception, chatId: String): List<BotApiMethod<*>> {
		logger.error("Handling exception: ${e::class.simpleName} - ${e.message}", e)
		val errorMessage = when {
			e.cause is BotException -> (e.cause as BotException).message.toString()
			e is BotException -> e.message.toString()
			else -> "Произошла непредвиденная ошибка. Попробуйте позже."
		}

		return listOf(SendMessage(chatId, errorMessage))
	}
}
