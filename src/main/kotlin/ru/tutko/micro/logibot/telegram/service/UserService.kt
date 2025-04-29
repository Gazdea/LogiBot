package ru.tutko.micro.logibot.telegram.service

import org.springframework.data.domain.Page
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import java.util.*

interface UserService {
    fun getUserById(id: Long): UserDto

    fun getUserOrganizationLinkByUserOrganizationData(organizationId: Long, userId: Long): UserOrganizationLinkDto

    fun getUserByUserExternalId(userId: Long): UserDto

    fun getUserByUsername(username: String): Optional<UserDto>

    fun createUser(user: UserDto): UserDto

    fun updateUser(user: UserDto): UserDto

    fun deleteUser(userId: Long)

    fun createIfNotExists(user: UserDto): UserDto

    fun exists(userId: Long): Boolean

    fun getUsersByOrganization(organizationId: Long, page: Int, size: Int = 8): Page<UserDto>

    fun getUsersByOrganizationIdAndPermissions(
        organizationId: Long,
        permissions: Collection<PermissionAccessEnum>,
        include: Boolean = true,
        page: Int,
        size: Int = 8
    ): Page<UserDto>

    fun updateUserRoleByOrganization(organizationId: Long, userId: Long, roleId: Long)

    fun joinUserOrganizationIfNeeded(organizationId: Long, userExternalId: Long): UserDto

}