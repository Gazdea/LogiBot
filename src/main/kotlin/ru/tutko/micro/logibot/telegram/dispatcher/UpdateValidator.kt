package ru.tutko.micro.logibot.telegram.dispatcher

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.filter.UpdateValidationFilter

@Component
class UpdateValidator(private val filters: List<UpdateValidationFilter>) {
	fun validate(update: Update) {
		filters.forEach { filter ->
			if (filter.validate(update)) filter.process(update)
		}
	}
}