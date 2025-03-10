package ru.tutko.micro.logibot.telegram.component

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendLocation
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import ru.tutko.micro.logibot.telegram.model.*

@Component
class TelegramBot (
    @Value("\${telegrambots.bots[0].token}") val myBotToken: String,
    @Value("\${telegrambots.bots[0].username}") val myBotUsername: String,
    private val telegramRequest: TelegramRequest
    ) : TelegramLongPollingBot(DefaultBotOptions(), myBotToken) {

    override fun getBotUsername(): String = myBotUsername

    override fun onUpdateReceived(update: Update) {
        sendResponse(telegramRequest.processUpdate(update))
    }

    private fun sendResponse(response: Response) {
        when (response) {
            is TextResponse -> sendTextMessage(response)
            is EditTextResponse -> editTextMessage(response)
            is EditKeyboardResponse -> editMessageReplyMarkup(response)
            is LocationResponse -> sendLocation(response)
            is WaitForInputResponse -> sendWaitForInputResponse(response)
        }
    }

    private fun sendWaitForInputResponse(response: WaitForInputResponse) {
        execute(SendMessage().apply {
            chatId = response.chatId
            text = response.text
        })
    }

    private fun sendTextMessage(response: TextResponse) {
        execute(SendMessage().apply {
            chatId = response.chatId
            text = response.text
            replyMarkup = response.buttons?.let { buildKeyboard(it) }
        })
    }

    private fun editTextMessage(response: EditTextResponse) {
        execute(EditMessageText().apply {
            chatId = response.chatId
            messageId = response.messageId
            text = response.text
            replyMarkup = response.buttons?.let { buildKeyboard(it) }
        })
    }

    private fun editMessageReplyMarkup(response: EditKeyboardResponse) {
        execute(EditMessageReplyMarkup().apply {
            chatId = response.chatId
            messageId = response.messageId
            replyMarkup = buildKeyboard(response.buttons)
        })
    }

    private fun sendLocation(response: LocationResponse) {
        execute(SendLocation().apply {
            chatId = response.chatId
            latitude = response.location.latitude
            longitude = response.location.longitude

        })
    }

    private fun buildKeyboard(buttons: List<Button>): InlineKeyboardMarkup {
        return InlineKeyboardMarkup(buttons.chunked(2).map { row ->
            row.map { button ->
                InlineKeyboardButton(button.text).apply {
                    callbackData = button.callbackData
                    url = button.url
                }
            }
        })
    }
}