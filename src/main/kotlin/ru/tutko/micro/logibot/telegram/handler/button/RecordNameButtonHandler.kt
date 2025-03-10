package ru.tutko.micro.logibot.telegram.handler.button

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.handler.ButtonHandlerInterface
import ru.tutko.micro.logibot.telegram.model.InputType
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.WaitForInputResponse

@Component
class RecordNameButtonHandler: ButtonHandlerInterface {
    override fun handleButtonPress(update: Update): Boolean {
        return update.callbackQuery.data == "record_name"
    }

    override fun getResponse(update: Update): Response {
        val chatId = update.callbackQuery.message.chatId.toString()
        return WaitForInputResponse(chatId, "Введите название:", InputType.NAME)
    }
}