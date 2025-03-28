package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import ru.tutko.micro.logibot.telegram.annotation.Handlers
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.CommandMapping
import ru.tutko.micro.logibot.telegram.model.Request
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum

@Handlers
class CancelHandler() {

    @CommandMapping(CommandEnum.CANCEL)
    fun handleCommandCancel(request: Request): Response {
        return Response(
            clearWaitingForInput = true,
            botApiMethods = listOf(SendMessage().apply {
                chatId = request.chatId.toString()
                text = "Ожидание ввода отменено." }),
            )
    }

    @CallbackMapping(CallbackQueryEnum.CANCEL)
    fun handleCallbackCancel(request: Request): Response {
        return Response(
            clearWaitingForInput = true,
            botApiMethods = listOf(SendMessage().apply {
                chatId =  request.chatId.toString()
                text = "Ожидание ввода отменено."}),
            )
    }
}