package ru.tutko.micro.logibot.telegram.exception

open class BotException(
    message: String = "Произошла ошибка в работе бота",
    cause: Throwable? = null
) : RuntimeException(message, cause)

class UnexpectedErrorException(message: String, cause: Throwable? = null) : BotException(message, cause)

// Ошибки валидации
open class ValidationException(message: String = "Ошибка валидации данных") : BotException(message)

class InvalidInputException(message: String = "Некорректный ввод данных") : ValidationException(message)

class MissingRequiredFieldException(message: String = "Отсутствует обязательное поле") : ValidationException(message)

// Ошибки авторизации и доступа
open class AuthorizationException(message: String = "Ошибка авторизации") : BotException(message)

class UnauthorizedAccessException(message: String = "Неавторизованный доступ") : AuthorizationException(message)

class ForbiddenActionException(message: String = "Доступ запрещен") : AuthorizationException(message)

// Ошибки, связанные с поиском
open class NotFoundException(message: String = "Ошибка, связанная с поиском") : BotException(message)

class UserNotFoundException(message: String = "Пользователь не найден") : NotFoundException(message)

class ChatNotFoundException(message: String = "Чат не найден") : NotFoundException(message)

class OrganizationNotFoundException(message: String = "Организация не найдена"): NotFoundException(message)

// Ошибки взаимодействия с внешними сервисами
open class ExternalServiceException(
    message: String = "Ошибка взаимодействия с внешним сервисом",
    cause: Throwable? = null
) : BotException(message, cause)

class TelegramApiRequestException(
    message: String = "Ошибка при обращении к Telegram API",
    cause: Throwable? = null
) : ExternalServiceException(message, cause)

class NetworkTimeoutException(message: String = "Время ожидания запроса истекло") : ExternalServiceException(message)

// Ошибки бизнес-логики
open class BusinessLogicException(message: String = "Ошибка бизнес-логики") : BotException(message)

class ActionNotAllowedException(message: String = "Действие недопустимо") : BusinessLogicException(message)

class InvalidOperationException(message: String = "Недопустимая операция") : BusinessLogicException(message)

// Ошибки инфраструктуры
open class InfrastructureException(
    message: String = "Ошибка инфраструктуры",
    cause: Throwable? = null
) : BotException(message, cause)

class DatabaseException(message: String = "Ошибка работы с базой данных") : InfrastructureException(message)

class DataNotFoundException(message: String = "Данные не найдены") : InfrastructureException(message)
