package ru.tutko.micro.logibot.telegram.aop

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.tutko.micro.logibot.telegram.exception.*

@ControllerAdvice
@RestController
class GlobalExceptionHandler {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(BotException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBotException(ex: BotException): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка: ${ex.message}"
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: ValidationException): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка валидации: ${ex.message}"
    }

    @ExceptionHandler(AuthorizationException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAuthorizationException(ex: AuthorizationException): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка доступа: ${ex.message}"
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleUserException(ex: NotFoundException): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка поиска: ${ex.message}"
    }

    @ExceptionHandler(ExternalServiceException::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun handleExternalServiceException(ex: ExternalServiceException): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка внешнего сервиса: ${ex.message}"
    }

    @ExceptionHandler(BusinessLogicException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleBusinessLogicException(ex: BusinessLogicException): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка бизнес-логики: ${ex.message}"
    }

    @ExceptionHandler(InfrastructureException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInfrastructureException(ex: InfrastructureException): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка инфраструктуры: ${ex.message}"
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleException(ex: Exception): String {
        log.error("Произошла ошибка: ${ex.message}", ex)
        return "Ошибка: ${ex.localizedMessage}"
    }
}