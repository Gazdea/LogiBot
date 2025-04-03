package ru.tutko.micro.logibot.telegram.mapper

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.model.entity.User
import ru.tutko.micro.logibot.telegram.utils.DtoTestBuilder
import ru.tutko.micro.logibot.telegram.utils.EntityTestBuilder

class UserMapperTest {
	private val userMapper = UserMapperImpl()
	private val entityTestBuilder = EntityTestBuilder()
	private val dtoTestBuilder = DtoTestBuilder()

	private var user = User()
	private var userDto = UserDto()

	@BeforeEach
	fun setUp() {
		user = entityTestBuilder.getUser()
		userDto = dtoTestBuilder.getUserDto()
	}

	@Test
	fun toEntity() {
		val mappedUser = userMapper.toEntity(userDto)

		assertAll({
			assertEquals(user.id, mappedUser.id)
			assertEquals(user.userId, mappedUser.userId)
			assertEquals(user.username, mappedUser.username)
			assertEquals(user.firstName, mappedUser.firstName)
			assertEquals(user.lastName, mappedUser.lastName)
		})
	}

	@Test
	fun toDto() {
		val mappedUser = userMapper.toDto(user)
		assertAll({
			assertEquals(userDto.id, mappedUser.id)
			assertEquals(userDto.userId, mappedUser.userId)
			assertEquals(userDto.username, mappedUser.username)
			assertEquals(userDto.firstName, mappedUser.firstName)
			assertEquals(userDto.lastName, mappedUser.lastName)
		})
	}
}