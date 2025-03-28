package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.CommandMapping
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.InputMapping
import ru.tutko.micro.logibot.telegram.model.CallbackData
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.service.OrganizationService
import ru.tutko.micro.logibot.telegram.util.TelegramUtil

@Handlers
class OrganizationHandler(
    private val organizationService: OrganizationService
) {

    @CommandMapping(CommandEnum.CREATE_ORGANIZATION)
    fun commandCreateOrganization(request: Request): Response {
        return Response(
            botApiMethods = listOf(
                SendMessage().apply {
                    chatId = request.chatId.toString()
                    text = "Введите название организации"
                    replyMarkup =
                        TelegramUtil.createInlineKeyboard("Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
                }
            ),
            inputType = CallbackData(InputEnum.CREATE_ORGANIZATION))
    }

    @InputMapping(InputEnum.CREATE_ORGANIZATION)
    fun callbackQueryCreateOrganization(request: Request): Response {
        val organizationName = request.update.message.text
        organizationService.createOrganization(organizationName, request.userId)
        return Response(
            clearWaitingForInput = true,
            botApiMethods = listOf(
                SendMessage().apply {
                    chatId = request.chatId.toString()
                    text =
                        "Организация $organizationName создана!\nДля продолжения работы с ботом создайте групповой чат и пригласите туда меня"
                }
            )
        )
    }

    @CallbackMapping(CallbackQueryEnum.CREATE_ORGANIZATION)
    fun callbackQueryUpdateOrganization(request: Request): Response {
        return Response(
            botApiMethods = listOf(
                EditMessageText().apply {
                    messageId = TelegramUtil.getMessage(request.update).messageId
                    chatId = request.chatId.toString()
                    text = "Введите название организации или отмените '/cancel'"
                    replyMarkup =
                        TelegramUtil.createInlineKeyboard("Отмена" to CallbackData(CallbackQueryEnum.CANCEL))
                }
            ),
            inputType = CallbackData(InputEnum.CREATE_ORGANIZATION.value)
        )
    }

    @CallbackMapping(CallbackQueryEnum.GET_ORGANIZATION)
    fun callbackQueryGetOrganization(request: Request): Response {
        val orgId = request.data?.data?.get("orgId") as Long
        val organization = organizationService.getOrganizationById(orgId)

        return Response(
            botApiMethods = listOf(
                EditMessageText().apply {
                    messageId = TelegramUtil.getMessage(request.update).messageId
                    chatId = request.chatId.toString()
                    text = "Ваша организация ${organization!!.name}"
                    replyMarkup =
                        TelegramUtil.createInlineKeyboard(
                            "Настройки" to CallbackData(CallbackQueryEnum.SETTINGS)
                        )
                }
            )
        )
    }

}