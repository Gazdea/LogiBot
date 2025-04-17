package ru.tutko.micro.logibot.telegram.service

import org.springframework.data.domain.Page
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import java.util.*

interface UserService {
    fun getUserById(id: Long): UserDto

    fun getUserByUserExternalId(userId: Long): UserDto

    fun getUserByUsername(username: String): Optional<UserDto>

    fun createUser(user: UserDto): UserDto

    fun updateUser(user: UserDto): UserDto

    fun deleteUser(userId: Long)

    fun createIfNotExists(user: UserDto): UserDto

    fun exists(userId: Long): Boolean

    fun getUsersByOrganization(organizationId: Long, page: Int, size: Int = 8): Page<UserDto>

}