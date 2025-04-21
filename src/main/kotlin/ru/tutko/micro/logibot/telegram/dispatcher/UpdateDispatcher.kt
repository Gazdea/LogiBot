package ru.tutko.micro.logibot.telegram.dispatcher

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.mapping.*
import ru.tutko.micro.logibot.telegram.aop.logging.LoggingAspect
import ru.tutko.micro.logibot.telegram.exception.*
import ru.tutko.micro.logibot.telegram.filter.ResponseValidationFilter
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum
import ru.tutko.micro.logibot.telegram.util.UpdateUtil
import ru.tutko.micro.logibot.telegram.filter.UpdateValidationFilter
import ru.tutko.micro.logibot.telegram.service.redis.CallbackRedisService
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Component
class UpdateDispatcher(
    private val handlerMethods: List<Pair<Any, KFunction<*>>>,
    private val updateValidationFilter: List<UpdateValidationFilter>,
    private val responseValidationFilter: List<ResponseValidationFilter>,

    private val redisService: CallbackRedisService
) {
    private val logger = LoggerFactory.getLogger(LoggingAspect::class.java)

    fun dispatch(update: Update): List<BotApiMethod<*>>? {
        val chatId = UpdateUtil(update).getChat().id
        val userId = UpdateUtil(update).getUser().id
        val chatIdUserId = "$chatId:$userId"
        val handlerType = UpdateUtil(update).getHandlerType()

        logger.info("Получен обновление для chatId: $chatId, userId: $userId, handlerType: $handlerType")

        for (filter in updateValidationFilter) {
            try {
                if (filter.validate(update)) filter.process(update)
            } catch (e: Exception) {
                logger.error("Ошибка при вызове входного фильтра ${filter}: ${e.message}", e)
                return handleError(e, chatId.toString())
            }
        }

        val callback = when (handlerType) {
            HandlerTypeEnum.INPUT -> {redisService.get(chatIdUserId)}
            HandlerTypeEnum.CALLBACK -> {redisService.get(update.callbackQuery.data)}
            else -> null
        }

        val request = Request(
            chatId,
            userId,
            update,
            callback
        )

        return try {

            logger.info("Поиск обработчика для типа $handlerType")

            handlerMethods
                .filter { (_, method) -> method.hasHandlerType(handlerType) }
                .firstOrNull { (_, method) ->
                    val annotation = method.getAnnotationByType(handlerType)
                    annotation != null && annotation.matches(request, handlerType)
                }
                ?.let { (handlerInstance, method) ->
                    val response = try {
                        method.call(handlerInstance, request) as? Response
                    } catch (e: Exception) {
                        logger.error("Ошибка при вызове обработчика ${method.name}: ${e.message}", e)
                        return handleError(e, chatId.toString())
                    }

                    if (response == null) return@let null

                    for (filter in responseValidationFilter) {
                        if (filter.validate(response)) filter.process(chatIdUserId, response)
                    }

                    if (update.hasCallbackQuery()) {
                        response.botApiMethods += AnswerCallbackQuery(update.callbackQuery.id)
                    }

                    logger.info("Обработчик ${method.name} вернул ответ: $response")

                    response.botApiMethods
                }
        } catch (e: Exception) {
            logger.error("Ошибка при обработке обновления для chatId: $chatId, userId: $userId", e)
            handleError(e, chatId.toString())
        }
    }

    private fun handleError(e: Exception, chatId: String): List<BotApiMethod<*>> {
        logger.error("Ошибка в боте: ${e.message}", e)

        val errorMessage = when (e.cause) {
            is ValidationException -> "Ошибка валидации: ${e.message}"
            is AuthorizationException -> "Ошибка авторизации: ${e.message}"
            is NotFoundException -> "Ошибка поиска: ${e.message}"
            is ExternalServiceException -> "Ошибка взаимодействия с внешним сервисом: ${e.message}"
            is BusinessLogicException -> "Ошибка бизнес-логики: ${e.message}"
            is InfrastructureException -> "Ошибка инфраструктуры: ${e.message}"
            is BotException -> e.message.toString()
            else -> "Произошла непредвиденная ошибка. Попробуйте позже."
        }

        return listOf(SendMessage(chatId, errorMessage))
    }

    private fun KFunction<*>.getAnnotationByType(handlerType: HandlerTypeEnum): Annotation? {
        return when (handlerType) {
            HandlerTypeEnum.COMMAND -> findAnnotation<CommandMapping>()
            HandlerTypeEnum.CALLBACK -> findAnnotation<CallbackMapping>()
            HandlerTypeEnum.INPUT -> findAnnotation<InputMapping>()
            HandlerTypeEnum.CHAT_MEMBER -> findAnnotation<ChatMemberMapping>()
            HandlerTypeEnum.MY_CHAT_MEMBER -> findAnnotation<MyChatMemberMapping>()

            HandlerTypeEnum.UNKNOWN -> null
        }
    }

    private fun KFunction<*>.hasHandlerType(handlerType: HandlerTypeEnum): Boolean {
        return when (handlerType) {
            HandlerTypeEnum.COMMAND -> hasAnnotation<CommandMapping>()
            HandlerTypeEnum.CALLBACK -> hasAnnotation<CallbackMapping>()
            HandlerTypeEnum.INPUT -> hasAnnotation<InputMapping>()
            HandlerTypeEnum.CHAT_MEMBER -> hasAnnotation<ChatMemberMapping>()
            HandlerTypeEnum.MY_CHAT_MEMBER -> hasAnnotation<MyChatMemberMapping>()

            HandlerTypeEnum.UNKNOWN -> false
        }
    }

    private fun Annotation.matches(request: Request, handlerType: HandlerTypeEnum): Boolean {
        return when (this) {
            is CommandMapping -> handlerType == HandlerTypeEnum.COMMAND && request.update.message.text == this.command.value
            is CallbackMapping -> handlerType == HandlerTypeEnum.CALLBACK && request.data?.handler == this.callbackQuery.value
            is InputMapping -> handlerType == HandlerTypeEnum.INPUT && request.data?.handler == this.input.value
            is ChatMemberMapping -> handlerType == HandlerTypeEnum.CHAT_MEMBER && request.update.chatMember?.newChatMember?.status == this.chatMember.value
            is MyChatMemberMapping -> handlerType == HandlerTypeEnum.MY_CHAT_MEMBER && request.update.myChatMember?.newChatMember?.status == this.myChatMember.value

            else -> false
        }
    }
}