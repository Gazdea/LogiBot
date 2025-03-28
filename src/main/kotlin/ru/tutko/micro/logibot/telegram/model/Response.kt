package ru.tutko.micro.logibot.telegram.model

import kotlinx.serialization.Serializable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum

/**
 * Универсальный класс ответа.
 */
data class Response(
    var botApiMethods: List<BotApiMethod<*>>,
    val clearWaitingForInput: Boolean = false,
    val inputType: CallbackData? = null,
)