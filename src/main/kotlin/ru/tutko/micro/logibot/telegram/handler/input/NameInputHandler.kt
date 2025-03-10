package ru.tutko.micro.logibot.telegram.handler.input

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.handler.InputHandlerInterface
import ru.tutko.micro.logibot.telegram.model.InputType
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.TextResponse

@Component
class NameInputHandler : InputHandlerInterface {
    override fun handleInput(update: Update): Response {
        val chatId = update.message.chatId.toString()
        val name = update.message.text
        // Здесь ты можешь сохранить имя (например, в базу данных)
        return TextResponse(chatId, "Имя '$name' сохранено!")
    }

    override fun getInputType(): InputType {
        return InputType.NAME
    }
}