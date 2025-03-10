package ru.tutko.micro.logibot.telegram.handler.button

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.ButtonHandler
import ru.tutko.micro.logibot.telegram.handler.ButtonHandlerInterface
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.LocationResponse
import ru.tutko.micro.logibot.telegram.model.Location

@Component
class MoreInfoButtonHandler: ButtonHandlerInterface {
    override fun handleButtonPress(update: Update): Boolean {
        return update.callbackQuery.data == "more_info"
    }

    override fun getResponse(update: Update): Response {
        val chatId = update.callbackQuery.message.chatId.toString()
        val messageId = update.callbackQuery.message.messageId

        return LocationResponse(
            chatId = chatId,
            location = Location(
                latitude = 55.4871,
                longitude = 28.7856
            )
        )

    }

}