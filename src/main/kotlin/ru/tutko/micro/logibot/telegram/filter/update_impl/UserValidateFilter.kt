package ru.tutko.micro.logibot.telegram.filter.update_impl

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.service.UserService
import ru.tutko.micro.logibot.telegram.util.UpdateUtil
import ru.tutko.micro.logibot.telegram.filter.UpdateValidationFilter

@Component
class UserValidateFilter(
	private val userService: UserService
): UpdateValidationFilter {

	override fun validate(update: Update): Boolean {
		val user = UpdateUtil(update).getUser()
		return !userService.exists(user.id)
	}

	override fun process(update: Update) {
		val user = UpdateUtil(update).getUser()
		userService.createUser(
			UserDto(
				externalUserId = user.id,
				username = user.userName,
				firstName = user.firstName,
				lastName = user.lastName
			)
		)
	}
}