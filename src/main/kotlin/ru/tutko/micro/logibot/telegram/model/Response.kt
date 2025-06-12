package ru.tutko.micro.logibot.telegram.model

import kotlinx.serialization.Serializable
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import ru.tutko.micro.logibot.telegram.model.data.Payload
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum

/**
 * Универсальный класс ответа.
 */
data class Response(
    var botApiMethods: List<BotApiMethod<*>>,
    var partialBotApiMethod: List<PartialBotApiMethod<*>>? = null,
    val clearWaitingForInput: Boolean = false,
    val inputType: CallbackData<Payload>? = null,
)

