package ru.tutko.micro.logibot.telegram.model

/**
 * Базовый sealed-класс для различных типов ответов.
 */
sealed class Response(open val chatId: String)

/**
 * Отправка нового текстового сообщения.
 */
data class TextResponse(
    override val chatId: String,
    val text: String,
    val buttons: List<Button>? = null,
    val replyOptions: ReplyOptions? = null
) : Response(chatId)

/**
 * Редактирование уже существующего текстового сообщения.
 */
data class EditTextResponse(
    override val chatId: String,
    val messageId: Int,
    val text: String,
    val buttons: List<Button>? = null
) : Response(chatId)

/**
 * Изменение только кнопок без изменения текста сообщения.
 */
data class EditKeyboardResponse(
    override val chatId: String,
    val messageId: Int,
    val buttons: List<Button>
) : Response(chatId)

/**
 * Отправка геолокации.
 */
data class LocationResponse(
    override val chatId: String,
    val location: Location
) : Response(chatId)

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
    val text: String,
    val inputType: InputType
) : Response(chatId)

/**
 * Типы ожидаемого ввода.
 */
enum class InputType {
    ORGANIZATION_NAME,
    NAME
}