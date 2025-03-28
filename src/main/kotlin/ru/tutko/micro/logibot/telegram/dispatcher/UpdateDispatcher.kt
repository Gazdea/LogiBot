package ru.tutko.micro.logibot.telegram.dispatcher

import ru.tutko.micro.logibot.telegram.component.TelegramSerialize
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.mapping.*
import ru.tutko.micro.logibot.telegram.component.WaitingForInputContextStorage
import ru.tutko.micro.logibot.telegram.filter.ResponseValidationFilter
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.enums.mapping.HandlerTypeEnum
import ru.tutko.micro.logibot.telegram.util.TelegramUtil
import ru.tutko.micro.logibot.telegram.filter.UpdateValidationFilter
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Component
class UpdateDispatcher(
    private val handlerMethods: List<Pair<Any, KFunction<*>>>,
    private val updateValidationFilter: List<UpdateValidationFilter>,
    private val responseValidationFilter: List<ResponseValidationFilter>,

    private val waitingForInputContextStorage: WaitingForInputContextStorage
) {

    fun dispatch(update: Update): List<BotApiMethod<*>>? {
        val chatId = TelegramUtil.getChatId(update)
        val userId = TelegramUtil.getUserId(update)
        val chatIdUserId = "$chatId:$userId"
        val handlerType = TelegramUtil.getHandlerType(update)

        for (filter in updateValidationFilter) {
            if (filter.validate(update)) filter.process(update)
        }

        val request = Request(
            chatId,
            userId,
            update,
            TelegramSerialize.extractData(update, handlerType, waitingForInputContextStorage.get(chatIdUserId))
        )

        return handlerMethods
            .filter { (_, method) -> method.hasHandlerType(handlerType) }
            .firstOrNull { (_, method) ->
                val annotation = method.getAnnotationByType(handlerType)
                annotation != null && annotation.matches(request, handlerType)
            }
            ?.let { (handlerInstance, method) ->
                val response = method.call(
                    handlerInstance,
                    request
                ) as? Response ?: return@let null

                for (filter in responseValidationFilter) {
                    if (filter.validate(response)) filter.process(chatIdUserId, response)
                }

                if (update.hasCallbackQuery()) {
                    response.botApiMethods += AnswerCallbackQuery(update.callbackQuery.id)
                }
                response.botApiMethods
            }
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
            is ChatMemberMapping -> handlerType == HandlerTypeEnum.CHAT_MEMBER && request.update.chatMember.newChatMember.status == this.chatMember.value
            is MyChatMemberMapping -> handlerType == HandlerTypeEnum.MY_CHAT_MEMBER && request.update.chatMember.newChatMember.status == this.myChatMember.value

            else -> false
        }
    }
}