package ru.tutko.micro.logibot.telegram.service

import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import java.util.*

interface UserService {
    fun getUserById(id: Long): Optional<UserDto>

    fun getUserByUserId(userId: Long): Optional<UserDto>

    fun getUserByUsername(username: String): Optional<UserDto>

    fun createUser(user: UserDto): UserDto

    fun updateUser(user: UserDto): UserDto

    fun deleteUser(userId: Long)

    fun createIfNotExists(user: UserDto): UserDto

    fun exists(userId: Long): Boolean
}