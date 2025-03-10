package ru.tutko.micro.logibot.telegram.handler.organization

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.handler.InputHandlerInterface
import ru.tutko.micro.logibot.telegram.model.InputType
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.TextResponse
import ru.tutko.micro.logibot.telegram.service.OrganizationService

@Component
class CreateOrganizationInputHandler(private val organizationService: OrganizationService): InputHandlerInterface {
    override fun getInputType(): InputType {
        return InputType.ORGANIZATION_NAME
    }

    override fun handleInput(update: Update): Response {
        val chatId = update.message.chatId.toString()
        val name = update.message.text
        val organizationDto = organizationService.createOrganization(name)
        return TextResponse(chatId, "Имя '${organizationDto!!.name}' сохранено!")
    }
}