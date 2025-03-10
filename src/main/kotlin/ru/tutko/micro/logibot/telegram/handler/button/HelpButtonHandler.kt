package ru.tutko.micro.logibot.telegram.handler.button

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.handler.ButtonHandlerInterface
import ru.tutko.micro.logibot.telegram.model.*

@Component
class HelpButtonHandler : ButtonHandlerInterface {

    override fun handleButtonPress(update: Update): Boolean {
        return update.callbackQuery.data == "help"
    }

    override fun getResponse(update: Update): Response {
        val chatId = update.callbackQuery.message.chatId.toString()
        val messageId = update.callbackQuery.message.messageId

        return EditTextResponse(
            chatId = chatId,
            messageId = messageId,
            text = "Вот информация по справке: \n /start",
            buttons = listOf(
                Button(text = "Подробнее", callbackData = "more_info"),
                Button(text = "Закрыть", callbackData = "close"),
                Button(text = "Записать имя", callbackData = "record_name")
            )
        )
    }
}
