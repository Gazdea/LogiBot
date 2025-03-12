package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.annotation.BotHandlers
import ru.tutko.micro.logibot.telegram.annotation.CallbackHandler
import ru.tutko.micro.logibot.telegram.annotation.CommandHandler
import ru.tutko.micro.logibot.telegram.annotation.InputHandler
import ru.tutko.micro.logibot.telegram.model.*
import ru.tutko.micro.logibot.telegram.model.enums.BotCallbackQuery
import ru.tutko.micro.logibot.telegram.model.enums.BotCommand
import ru.tutko.micro.logibot.telegram.model.enums.BotInput

@BotHandlers
class TestHandler
{

    @CommandHandler(BotCommand.TEST)
    fun test(update: Update): Response {
        val chatId = update.message.chatId.toString()
        val userId = update.message.from.id.toString()

        return TextResponse(
            chatId = chatId,
            userId = userId,
            text = "Проверка работоспособности",
            buttons = listOf(
                Button(text = "Проверка input", callbackData = BotCallbackQuery.TEST_INPUT.value),
                Button(text = "Проверка карт", callbackData = BotCallbackQuery.TEST_MAP.value),
                Button(text = "Закрыть", callbackData = BotCallbackQuery.TEST_CLOSE.value),
            )
            )
    }

    @CallbackHandler(BotCallbackQuery.TEST_INPUT)
    fun testCallback(update: Update): Response {
        val chatId = update.callbackQuery.message.chatId.toString()
        val userId = update.callbackQuery.from.id.toString()

        return WaitForInputResponse(
            chatId = chatId,
            userId = userId,
            text = "Ожидается сообщение",
            inputType = BotInput.TEST
        )
    }

    @InputHandler(BotInput.TEST)
    fun testInput(update: Update): Response {
        val chatId = update.message.chatId.toString()
        val userId = update.message.from.id.toString()
        return TextResponse(
            chatId = chatId,
            userId = userId,
            text = "я прочитал ${update.message.text}",
            clearWaitingForInout = true
        )
    }

    @CallbackHandler(BotCallbackQuery.TEST_MAP)
    fun testMap(update: Update): Response {
        val chatId = update.callbackQuery.message.chatId.toString()
        val userId = update.callbackQuery.from.id.toString()

        return LocationResponse(
            chatId = chatId,
            userId = userId,
            location = Location(
                latitude = 1242.1,
                longitude = 221.4
            )
        )
    }

    @CallbackHandler(BotCallbackQuery.TEST_CLOSE)
    fun testClose(update: Update): Response {
        val chatId = update.callbackQuery.message.chatId.toString()
        val userId = update.callbackQuery.from.id.toString()
        val messageId = update.callbackQuery.message.messageId

        return EditTextResponse(
            chatId = chatId,
            userId = userId,
            messageId = messageId,
            text = "Закрыто"
        )
    }
}