package ru.tutko.micro.logibot.telegram.model

import ru.tutko.micro.logibot.telegram.model.enums.BotCallbackQuery
import ru.tutko.micro.logibot.telegram.model.enums.BotInput

/**
 * Базовый sealed-класс для различных типов ответов.
 */
sealed class Response(
    open val chatId: String,
    open val userId: String,
    open val clearWaitingForInout: Boolean = false
)

/**
 * Отправка нового текстового сообщения.
 */
data class TextResponse(
    override val chatId: String,
    override val userId: String,
    override val clearWaitingForInout: Boolean = false,
    val text: String,
    val buttons: List<Button>? = null,
    val replyOptions: ReplyOptions? = null,
) : Response(chatId, userId)

data class CancelWaitingForInputResponse(
    override val chatId: String,
    override val userId: String,
    val text: String,
) : Response(chatId, userId)
/**
 * Редактирование уже существующего текстового сообщения.
 */
data class EditTextResponse(
    override val chatId: String,
    override val userId: String,
    val messageId: Int,
    val text: String,
    val buttons: List<Button>? = null
) : Response(chatId, userId)

/**
 * Изменение только кнопок без изменения текста сообщения.
 */
data class EditKeyboardResponse(
    override val chatId: String,
    override val userId: String,
    val messageId: Int,
    val buttons: List<Button>
) : Response(chatId, userId)

/**
 * Отправка геолокации.
 */
data class LocationResponse(
    override val chatId: String,
    override val userId: String,
    val location: Location
) : Response(chatId, userId)

/**
 * Модель кнопки.
 */
data class Button(
    val text: String,
    val callbackData: String? = null,
    val url: String? = null
)

/**
 * Геолокация.
 */
data class Location(
    val latitude: Double,
    val longitude: Double
)

/**
 * Опции для обычной клавиатуры.
 */
data class ReplyOptions(
    val keyboard: List<List<String>>,
    val resizeKeyboard: Boolean = true,
    val oneTimeKeyboard: Boolean = false,
    val selective: Boolean = false
)

/**
 * Ожидание следующего сообщения от пользователя.
 */
data class WaitForInputResponse(
    override val chatId: String,
    override val userId: String,
    val text: String,
    val inputType: BotInput,
    val buttons: List<Button> = listOf(Button("Отмена", BotCallbackQuery.CANCEL.value)),
) : Response(chatId, userId)