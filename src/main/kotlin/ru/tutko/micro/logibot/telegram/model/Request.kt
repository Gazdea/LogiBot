package ru.tutko.micro.logibot.telegram.model

import org.telegram.telegrambots.meta.api.objects.Update

data class Request(
	val chatId: Long,
	val userId: Long,
	val update: Update,
	val data: CallbackData? = null,
)
