package ru.tutko.micro.logibot.telegram.telegram_update

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.tutko.micro.logibot.telegram.dispatcher.UpdateDispatcher
import ru.tutko.micro.logibot.telegram.exception.*
import java.io.Serializable
import java.lang.reflect.InvocationTargetException

@Component
class TelegramBot (
    @Value("\${telegrambots.bots[0].token}") val myBotToken: String,
    @Value("\${telegrambots.bots[0].username}") val myBotUsername: String,
    private val updateDispatcher: UpdateDispatcher,
    ) : TelegramLongPollingBot(DefaultBotOptions(), myBotToken) {

    override fun getBotUsername(): String = myBotUsername

    override fun onUpdateReceived(update: Update) {
        try {
            updateDispatcher.dispatch(update)?.let { executeAll(it) }
        } catch (ex: BotException) {
            handleException(ex, update)
        }
    }

    fun executeAll(methods: List<BotApiMethod<*>>) {
        methods.forEach { execute(it as BotApiMethod<Serializable>) }
    }

    private fun handleException(ex: Throwable, update: Update? = null) {
        val botException = when (ex) {
            is BotException -> ex
            is InvocationTargetException -> ex.cause as? BotException ?: UnexpectedErrorException("Ошибка выполнения команды", ex)
            else -> UnexpectedErrorException("Неизвестная ошибка", ex)
        }

        println("Ошибка: ${botException.message}") // Логирование

        update?.let {
            val chatId = it.message?.chatId ?: return
            val errorMessage = SendMessage(chatId.toString(), "Произошла ошибка: ${botException.message}")
            try {
                execute(errorMessage)
            } catch (e: TelegramApiException) {
                println("Ошибка при отправке сообщения об ошибке: ${e.message}")
            }
        }
    }

}