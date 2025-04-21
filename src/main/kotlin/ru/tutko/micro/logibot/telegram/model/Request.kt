package ru.tutko.micro.logibot.telegram.model

import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.data.Payload

data class Request(
	val chatId: Long,
	val userId: Long,
	val update: Update,
	val data: CallbackData<Payload>? = null,
)
