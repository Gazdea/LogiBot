package ru.tutko.micro.logibot.telegram.dispatcher

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.BotHandlers
import ru.tutko.micro.logibot.telegram.annotation.CallbackHandler
import ru.tutko.micro.logibot.telegram.annotation.CommandHandler
import ru.tutko.micro.logibot.telegram.annotation.InputHandler
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.TextResponse
import ru.tutko.micro.logibot.telegram.model.WaitForInputResponse
import ru.tutko.micro.logibot.telegram.model.enums.BotInput
import ru.tutko.micro.logibot.telegram.model.enums.HandlerType
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

@Component
class UpdateDispatcher(
    private val applicationContext: ApplicationContext
) {
    private val handlerMethods: List<Pair<Any, KFunction<*>>> = initHandlerMethods()
    private val waitingForInput: MutableMap<String, BotInput> = mutableMapOf()

    private fun initHandlerMethods(): List<Pair<Any, KFunction<*>>> {
        return applicationContext.getBeansWithAnnotation(BotHandlers::class.java)
            .values // получаем все бины с аннотацией @BotHandlers
            .flatMap { handler ->
                handler::class.declaredMemberFunctions
                    .filter { method ->
                        method.hasAnnotation<CommandHandler>() ||
                                method.hasAnnotation<CallbackHandler>() ||
                                method.hasAnnotation<InputHandler>()
                    }
                    .map { method -> handler to method } // связываем методы с их объектами
            }
    }

    fun dispatch(update: Update): Response? {
        val chatId = getChatId(update)
        val userId = getUserId(update)
        val chatIdUserId = "${chatId}:${userId}"
        val handlerType = getHandlerType(update)
        println(waitingForInput.toString())
        // Обычная обработка команд, колбэков и ввода
        return handlerMethods
            .filter { (_, method) -> method.hasHandlerType(handlerType) }
            .firstOrNull { (handlerInstance, method) ->
                val annotation = method.getAnnotationByType(handlerType)
                annotation != null && annotation.matches(update)
            }
            ?.let { (handlerInstance, method) ->
                val response = method.call(handlerInstance, update) as Response
                if (response.clearWaitingForInout) {
                    waitingForInput.remove(chatIdUserId)
                }
                if (response is WaitForInputResponse) {
                    waitingForInput[chatIdUserId] = response.inputType
                }

                response
            }
    }

    private fun KFunction<*>.getAnnotationByType(handlerType: HandlerType): Annotation? {
        return when (handlerType) {
            HandlerType.COMMAND -> findAnnotation<CommandHandler>()
            HandlerType.CALLBACK -> findAnnotation<CallbackHandler>()
            HandlerType.INPUT -> findAnnotation<InputHandler>()
            HandlerType.UNKNOWN -> null
        }
    }

    private fun KFunction<*>.hasHandlerType(handlerType: HandlerType): Boolean {
        return when (handlerType) {
            HandlerType.COMMAND -> hasAnnotation<CommandHandler>()
            HandlerType.CALLBACK -> hasAnnotation<CallbackHandler>()
            HandlerType.INPUT -> hasAnnotation<InputHandler>()
            HandlerType.UNKNOWN -> false
        }
    }

    private fun getChatId(update: Update): String {
        return when {
            update.hasMessage() -> update.message.chatId.toString()
            update.hasCallbackQuery() -> update.callbackQuery.message.chatId.toString()
            update.hasChatJoinRequest() -> update.chatJoinRequest.chat.id.toString()
            update.hasPoll()-> update.poll.id
            update.hasPollAnswer()-> update.pollAnswer.user.id.toString()
            update.hasPreCheckoutQuery()-> update.preCheckoutQuery.id
            update.hasShippingQuery()-> update.shippingQuery.id
            update.hasEditedMessage() -> update.editedMessage.chatId.toString()
            update.hasEditedChannelPost() -> update.editedChannelPost.chatId.toString()
            update.hasChannelPost() -> update.channelPost.chatId.toString()

            else -> "0"
        }
    }

    private fun getUserId(update: Update): String {
        return when {
            update.hasMessage() -> update.message.from.id.toString()
            update.hasCallbackQuery() -> update.callbackQuery.from.id.toString()
            update.hasChatJoinRequest() -> update.chatJoinRequest.user.id.toString()
            update.hasPoll()-> update.poll.id.toString()
            update.hasPollAnswer()-> update.pollAnswer.user.id.toString()
            update.hasPreCheckoutQuery()-> update.preCheckoutQuery.id.toString()
            update.hasShippingQuery()-> update.shippingQuery.id.toString()
            update.hasEditedMessage() -> update.editedMessage.from.id.toString()
            update.hasEditedChannelPost() -> update.editedChannelPost.from.id.toString()
            update.hasChannelPost() -> update.channelPost.from.id.toString()

            else -> "0"
        }
    }

    private fun getHandlerType(update: Update): HandlerType {
        return when{
            update.hasMessage() && update.message.text.toString().startsWith("/")-> HandlerType.COMMAND
            update.hasCallbackQuery() -> HandlerType.CALLBACK
            update.hasMessage() && !update.message.text.toString().startsWith("/") -> HandlerType.INPUT

            else -> HandlerType.UNKNOWN
        }
    }

    private fun Annotation.matches(update: Update): Boolean {
        val handlerType = getHandlerType(update)

        return when (this) {
            is CommandHandler -> handlerType == HandlerType.COMMAND && update.message.text == this.command.value
            is CallbackHandler -> handlerType == HandlerType.CALLBACK && update.callbackQuery.data == this.callbackQuery.value
            is InputHandler -> handlerType == HandlerType.INPUT && waitingForInput.containsKey("${getChatId(update)}:${getUserId(update)}")
            else -> false
        }
    }
}