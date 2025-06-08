package ru.tutko.micro.logibot.telegram.dispatcher

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Component
class UpdateDispatcher(
    private val requestFactory: RequestFactory,
    private val validator: UpdateValidator,
    private val resolver: HandlerResolver,
    private val invoker: HandlerInvoker,
    private val processor: ResponseProcessor,
    private val errorHandler: ErrorHandler
) {
    private val logger = LoggerFactory.getLogger(UpdateDispatcher::class.java)

    fun dispatch(update: Update): Response? {
        val chatId = UpdateUtil(update).getChat().id
        return try {
            validator.validate(update)
            val request = requestFactory.create(update)
            val handler = resolver.resolve(request, request.handler) ?: return null
            val response = invoker.invoke(handler.first, handler.second, request)

            processor.process(response, "${request.chatId}:${request.userId}")

            if (update.hasCallbackQuery()) {
                response.botApiMethods += AnswerCallbackQuery(update.callbackQuery.id)
            }

            response
        } catch (e: Exception) {
            logger.error("Ошибка при обработке update: ${e.message}", e)
            errorHandler.handle(e, chatId.toString())
            null
        }
    }

}