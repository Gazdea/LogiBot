package ru.tutko.micro.logibot.telegram.dispatcher

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.tutko.micro.logibot.telegram.exception.*

@Component
class ErrorHandler {
	fun handle(e: Exception, chatId: String): List<BotApiMethod<*>> {
		val errorMessage = when (e) {
			is ValidationException -> "Ошибка валидации: ${e.message}"
			is AuthorizationException -> "Ошибка авторизации: ${e.message}"
			is NotFoundException -> "Ошибка поиска: ${e.message}"
			is ExternalServiceException -> "Ошибка внешнего сервиса: ${e.message}"
			is BusinessLogicException -> "Ошибка бизнес-логики: ${e.message}"
			is InfrastructureException -> "Ошибка инфраструктуры: ${e.message}"
			is BotException -> e.message.toString()
			else -> "Произошла непредвиденная ошибка. Попробуйте позже."
		}

		return listOf(SendMessage(chatId, errorMessage))
	}
}
