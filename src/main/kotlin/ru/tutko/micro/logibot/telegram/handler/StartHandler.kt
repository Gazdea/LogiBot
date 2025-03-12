package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.BotHandlers
import ru.tutko.micro.logibot.telegram.annotation.CallbackHandler
import ru.tutko.micro.logibot.telegram.annotation.CommandHandler
import ru.tutko.micro.logibot.telegram.annotation.InputHandler
import ru.tutko.micro.logibot.telegram.model.Response
import ru.tutko.micro.logibot.telegram.model.TextResponse
import ru.tutko.micro.logibot.telegram.model.enums.BotCallbackQuery
import ru.tutko.micro.logibot.telegram.model.enums.BotCommand
import ru.tutko.micro.logibot.telegram.model.enums.BotInput
import ru.tutko.micro.logibot.telegram.model.enums.PermissionAccess

@BotHandlers
class StartHandler {

    @CommandHandler(command = BotCommand.START, access = PermissionAccess.DEFAULT)
    fun handleStart(update: Update): Response {
        return TextResponse(update.message.chatId.toString(), update.message.from.id.toString(), text = "👋 Привет! Я бот.")
    }

    @CallbackHandler(callbackQuery = BotCallbackQuery.SETTINGS, access = PermissionAccess.DEFAULT)
    fun handleSettings(update: Update): Response {
        return TextResponse(update.callbackQuery.message.chatId.toString(), update.callbackQuery.from.id.toString(), text =  "⚙ Настройки")
    }
}