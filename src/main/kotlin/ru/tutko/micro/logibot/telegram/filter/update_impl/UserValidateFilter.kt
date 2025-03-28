package ru.tutko.micro.logibot.telegram.filter.update_impl

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.dto.UserDto
import ru.tutko.micro.logibot.telegram.service.UserService
import ru.tutko.micro.logibot.telegram.util.TelegramUtil
import ru.tutko.micro.logibot.telegram.filter.UpdateValidationFilter

@Component
class UserValidateFilter(
	private val userService: UserService
): UpdateValidationFilter {

	override fun validate(update: Update): Boolean {
		val from = TelegramUtil.getFrom(update)
		return !userService.exists(from.id)
	}

	override fun process(update: Update) {
		val from = TelegramUtil.getFrom(update)
		userService.createUser(
			UserDto(
				userId = from.id,
				username = from.userName,
				firstName = from.firstName,
				lastName = from.lastName
			)
		)
	}
}