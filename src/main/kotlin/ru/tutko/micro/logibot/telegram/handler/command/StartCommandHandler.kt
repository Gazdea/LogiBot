package ru.tutko.micro.logibot.telegram.handler.command

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.CommandHandler
import ru.tutko.micro.logibot.telegram.handler.CommandHandlerInterface
import ru.tutko.micro.logibot.telegram.model.Button
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.TextResponse

@Component
class StartCommandHandler : CommandHandlerInterface {
    override fun handle(update: Update): Boolean {
        val entities = update.message.entities
        return entities?.any { it.type == "bot_command" && it.text == "/start" } ?: false
    }

    override fun getResponse(update: Update): Response {
        val chatId = update.message.chatId.toString()
        return TextResponse(chatId, "Привет!", buttons = listOf(
            Button(text = "Помощь", callbackData = "help"),
        ))
    }
}