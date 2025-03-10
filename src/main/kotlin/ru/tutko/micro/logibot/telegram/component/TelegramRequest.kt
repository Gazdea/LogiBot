package ru.tutko.micro.logibot.telegram.component

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.handler.ButtonHandlerInterface
import ru.tutko.micro.logibot.telegram.handler.CommandHandlerInterface
import ru.tutko.micro.logibot.telegram.handler.InputHandlerInterface
import ru.tutko.micro.logibot.telegram.model.*

@Component
class TelegramRequest(
    private val commandHandlers: List<CommandHandlerInterface>,
    private val buttonHandlers: List<ButtonHandlerInterface>,
    private val inputHandlers: List<InputHandlerInterface>
) {
    private val waitingForInput: MutableMap<String, InputType> = mutableMapOf()

    fun processUpdate(update: Update): Response {
        return when {
            update.hasMessage() -> processMessage(update)
            update.hasCallbackQuery() -> processButtonPress(update)
            update.hasInlineQuery() -> processInlineQuery(update)
            update.hasChosenInlineQuery() -> processChosenInlineQuery(update)
            update.hasMyChatMember() -> processMyChatMember(update)
            update.hasChatMember() -> processChatMember(update)
            update.hasChatJoinRequest() -> processChatJoinRequest(update)
            update.hasPoll() -> processPoll(update)
            update.hasPollAnswer()-> processPollAnswer(update)
            update.hasPreCheckoutQuery()-> processPreCheckoutQuery(update)
            update.hasShippingQuery() -> processShippingQuery(update)
            update.hasEditedMessage() -> processEditedMessage(update)
            update.hasEditedChannelPost() -> processEditedChannelPost(update)
            update.hasChannelPost() -> processChannelPost(update)

            else -> TextResponse(chatId = getChatId(update), text = "Неизвестное действие")
        }
    }

    private fun processChannelPost(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Новое сообщение в канале")
    }

    private fun processEditedChannelPost(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Сообщение в канале было изменено")
    }

    private fun processEditedMessage(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Сообщение было изменено")
    }

    private fun processShippingQuery(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Запрос доставки")
    }

    private fun processPreCheckoutQuery(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Запрос оплаты")
    }

    private fun processPollAnswer(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Новый ответ в опросе")
    }

    private fun processPoll(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Новый опрос")
    }

    private fun processChatJoinRequest(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Запрос на присоединение к чату")
    }

    private fun processChatMember(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Пользователь '$update.message.from.firstName' присоединился к чату")
    }

    private fun processMyChatMember(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Бот Присоединился к чату")
    }

    private fun processChosenInlineQuery(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Неизвестное действие")
    }

    private fun processInlineQuery(update: Update): Response {
        return TextResponse(chatId = getChatId(update), text = "Неизвестное действие")
    }

    private fun processMessage(update: Update): Response {
        val chatId = update.message.chatId.toString()
        val inputType = waitingForInput[chatId]

        return if (inputType != null) {
            val handler = inputHandlers.find { it.getInputType() == inputType }
            if (handler != null) {
                waitingForInput.remove(chatId)
                handler.handleInput(update)
            } else {
                TextResponse(chatId = chatId, text = "Ошибка обработки ввода")
            }
        } else {
            for (handler in commandHandlers) {

                if (handler.handle(update)) {
                    val response = handler.getResponse(update)
                    if (response is WaitForInputResponse) {
                        waitingForInput[response.chatId] = response.inputType
                    }
                    return response
                }
            }
            TextResponse(chatId = chatId, text = "Неизвестная команда")
        }
    }

    private fun processButtonPress(update: Update): Response {
        for (handler in buttonHandlers) {
            if (handler.handleButtonPress(update)) {
                val response = handler.getResponse(update)
                if (response is WaitForInputResponse) {
                    waitingForInput[response.chatId] = response.inputType
                }
                return response
            }
        }
        return TextResponse(chatId = update.callbackQuery.message.chatId.toString(), text = "Неизвестная кнопка")
    }

    private fun getChatId(update: Update): String {
        return when {
            update.hasMessage() -> update.message.chatId.toString()
            update.hasCallbackQuery() -> update.callbackQuery.message.chatId.toString()
            update.hasChatJoinRequest() -> update.chatJoinRequest.chat.id.toString()
            update.hasPoll()-> update.poll.id
            update.hasPollAnswer()-> update.pollAnswer.user.id.toString()
            update.hasPreCheckoutQuery()-> update.preCheckoutQuery.id
            update.hasShippingQuery()-> update.shippingQuery.id
            update.hasEditedMessage() -> update.editedMessage.chatId.toString()
            update.hasEditedChannelPost() -> update.editedChannelPost.chatId.toString()
            update.hasChannelPost() -> update.channelPost.chatId.toString()

            else -> "0"
        }
    }

    private fun getUserId(update: Update): String {
        return when {
            update.hasMessage() -> update.message.from.id.toString()
            update.hasCallbackQuery() -> update.callbackQuery.message.from.id.toString()
            update.hasChatJoinRequest() -> update.chatJoinRequest.user.id.toString()
            update.hasPoll()-> update.poll.id.toString()
            update.hasPollAnswer()-> update.pollAnswer.user.id.toString()
            update.hasPreCheckoutQuery()-> update.preCheckoutQuery.id.toString()
            update.hasShippingQuery()-> update.shippingQuery.id.toString()
            update.hasEditedMessage() -> update.editedMessage.from.id.toString()
            update.hasEditedChannelPost() -> update.editedChannelPost.from.id.toString()
            update.hasChannelPost() -> update.channelPost.from.id.toString()

            else -> "0"
        }
    }
}