package ru.tutko.micro.logibot.telegram.handler.organization

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.handler.CommandHandlerInterface
import ru.tutko.micro.logibot.telegram.model.*

@Component
class CreateOrganizationCommandHandler: CommandHandlerInterface {
    override fun handle(update: Update): Boolean {
        val entities = update.message.entities
        return entities?.any { it.type == "bot_command" && it.text == "/create_organization" } ?: false
    }

    override fun getResponse(update: Update): Response {
        val chatId = update.message.chatId.toString()
        return WaitForInputResponse(chatId, "Введите название организации:", InputType.ORGANIZATION_NAME)
    }
}