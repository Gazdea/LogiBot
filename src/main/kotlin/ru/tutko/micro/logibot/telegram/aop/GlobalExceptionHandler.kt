package ru.tutko.micro.logibot.telegram.aop

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.tutko.micro.logibot.telegram.exception.*

@ControllerAdvice
@RestController
class GlobalExceptionHandler {

    @ExceptionHandler(BotException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBotException(ex: BotException): String {
        return "Ошибка: ${ex.message}"
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: ValidationException): String {
        return "Ошибка валидации: ${ex.message}"
    }

    @ExceptionHandler(AuthorizationException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAuthorizationException(ex: AuthorizationException): String {
        return "Ошибка доступа: ${ex.message}"
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserException(ex: NotFoundException): String {
        return "Ошибка поиска: ${ex.message}"
    }

    @ExceptionHandler(ExternalServiceException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun handleExternalServiceException(ex: ExternalServiceException): String {
        return "Ошибка внешнего сервиса: ${ex.message}"
    }

    @ExceptionHandler(BusinessLogicException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleBusinessLogicException(ex: BusinessLogicException): String {
        return "Ошибка бизнес-логики: ${ex.message}"
    }

    @ExceptionHandler(InfrastructureException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInfrastructureException(ex: InfrastructureException): String {
        return "Ошибка инфраструктуры: ${ex.message}"
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericException(ex: Exception): String {
        return "Неизвестная ошибка: ${ex.message}"
    }
}