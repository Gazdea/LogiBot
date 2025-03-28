package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.CommandMapping
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.util.TelegramUtil

@Handlers
class StartHandler(
    private val organizationService: OrganizationService
) {

    @CommandMapping(command = CommandEnum.START)
    fun handleStart(request: Request): Response {
        val organizations = organizationService.getOrganizationsByUserId(request.userId)
        return Response(
            botApiMethods = listOf(SendMessage().apply {
                chatId = request.chatId.toString()
                text = "👋 Привет! Вот организации в которых ты есть."
                replyMarkup = TelegramUtil.createInlineKeyboard(
                    organizations.map { org: OrganizationDto ->
                        org.name!! to CallbackData(CallbackQueryEnum.START_GET_ORGANIZATIONS, "orgId" to org.id!!)
                    },
                    listOf("Создать новую организацию" to CallbackData(CallbackQueryEnum.CREATE_ORGANIZATION))
                )
            })
        )
    }

    @CallbackMapping(callbackQuery = CallbackQueryEnum.SETTINGS)
    fun handleSettings(request: Request): Response {
        return Response(
            botApiMethods = listOf(
                SendMessage().apply {
                    chatId = request.chatId.toString()
                    text = "⚙ Настройки"
                })
        )
    }
}