package ru.tutko.micro.logibot.telegram.handler

import org.springframework.context.annotation.Lazy
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.BotHandlers
import ru.tutko.micro.logibot.telegram.annotation.CallbackHandler
import ru.tutko.micro.logibot.telegram.annotation.CommandHandler
import ru.tutko.micro.logibot.telegram.dispatcher.UpdateDispatcher
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.TextResponse
import ru.tutko.micro.logibot.telegram.model.enums.BotCallbackQuery
import ru.tutko.micro.logibot.telegram.model.enums.BotCommand

@BotHandlers
class CancelHandler() {

    @CommandHandler(BotCommand.CANCEL)
    fun handleCommandCancel(update: Update): Response {
        val chatId = update.message?.chatId.toString()
        val userId = update.message?.from?.id.toString()
        val chatIdUserId = "$chatId:$userId"

        return TextResponse(chatId, userId, text = "Ожидание ввода отменено.", clearWaitingForInout = true )
    }

    @CallbackHandler(BotCallbackQuery.CANCEL)
    fun handleCallbackCancel(update: Update): Response {
        val chatId = update.callbackQuery.message?.chatId.toString()
        val userId = update.callbackQuery.from?.id.toString()
        val chatIdUserId = "$chatId:$userId"

        return TextResponse(chatId, userId, text = "Ожидание ввода отменено.", clearWaitingForInout = true )
    }
}