package ru.tutko.micro.logibot.telegram.utils

import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.dto.*
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.DefaultRoleEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

@Component
class DtoTestBuilder {

    fun getChatDto(): ChatDto {
        return ChatDto(
            id = 1,
            chatId = 1,
            organization = null,
            type = ChatTypeEnum.GROUP.value,
            title = "title",
            username = "username"
        )
    }

    fun getOrganizationDto(): OrganizationDto {
        return OrganizationDto(
            id = 1,
            name = "name",
        )
    }

    fun getRoleDto(): RoleDto {
        return RoleDto(
            id = 1,
            role = DefaultRoleEnum.ADMIN.value,
            organizationId = 1,
            rolePermissions = mutableSetOf()
        )
    }

    fun getRolePermissionDto(): RolePermissionDto {
        return RolePermissionDto(
            id = 1,
            permission = PermissionAccessEnum.MANAGE_EMPLOYEES.value,
        )
    }

    fun getUserDto(): UserDto {
        return UserDto(
            id = 1,
            userId = 1,
            username = "username",
            firstName = "firstName",
            lastName = "lastName"
        )
    }

    fun getUserOrganizationLinkDto(): UserOrganizationLinkDto {
        return UserOrganizationLinkDto(
            id = getUserOrganizationLinkIdDto(),
            user = getUserDto(),
            organization = getOrganizationDto(),
            roleId = 1
        )
    }

    fun getUserOrganizationLinkIdDto(): UserOrganizationLinkIdDto {
        return UserOrganizationLinkIdDto(
            userId = 1,
            organizationId = 1,
        )
    }
}