package ru.tutko.micro.logibot.telegram.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.service.UserService

@RestController
@RequestMapping("/api")
class UserController(
	private val userService: UserService,
) {
	@GetMapping("/user-info/{userId}")
	fun getUserInfo(
		@PathVariable userId: Long
	): ResponseEntity<UserDto> {
		return ResponseEntity.ok(userService.getUserByUserExternalId(userId))
	}
}