package ru.tutko.micro.logibot.telegram.filter

import org.telegram.telegrambots.meta.api.objects.Update


interface UpdateValidationFilter {
	fun validate(update: Update):  Boolean

	fun process(update: Update)
}