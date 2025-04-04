package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.CommandMapping
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.data.OrganizationId
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil

@Handlers
class OrganizationHandler(
    private val organizationService: OrganizationService
) {
    @CallbackMapping(CallbackQueryEnum.GET_ORGANIZATION)
    fun callbackQueryGetOrganization(request: Request): Response {
        val organizationId =
            request.data?.getData<OrganizationId>() ?: throw ValidationException("Не найдена OrganizationId")
        val organization = organizationService.getOrganizationById(organizationId.orgId)

        val navigate = mutableListOf<Pair<String, CallbackData>>()

        navigate.add("Настройки" to CallbackData(CallbackQueryEnum.SETTINGS))

        val buttons = UpdateUtil.createInlineKeyboard(
            listOf(navigate)
        )
        return Response(
            botApiMethods = listOf(
                EditMessageText().apply {
                    messageId = UpdateUtil(request.update).getMessage()!!.messageId
                    chatId = request.chatId.toString()
                    text = "Ваша организация ${organization!!.name}"
                    replyMarkup = buttons
                }
            )
        )
    }
}