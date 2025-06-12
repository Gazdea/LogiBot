package ru.tutko.micro.logibot.telegram.telegram_update

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.tutko.micro.logibot.telegram.dispatcher.UpdateDispatcher
import java.io.Serializable

@Component
class TelegramBot (
    @Value("\${telegrambots.bots[0].token}") val myBotToken: String,
    @Value("\${telegrambots.bots[0].username}") val myBotUsername: String,
    private val updateDispatcher: UpdateDispatcher,
    ) : TelegramLongPollingBot(DefaultBotOptions(), myBotToken) {

    override fun getBotUsername(): String = myBotUsername

    override fun onUpdateReceived(update: Update) {
        updateDispatcher.dispatch(update)?.let { response ->
            executeAll(response.botApiMethods)
            executeAllPartial(response.partialBotApiMethod)
        }
    }

    fun executeAll(methods: List<BotApiMethod<*>>) {
        methods.forEach { execute(it as BotApiMethod<Serializable>) }
    }

    fun executeAllPartial(methods: List<PartialBotApiMethod<*>>?) {
        methods?.forEach { execute(it as SendDocument)
        }
    }

}