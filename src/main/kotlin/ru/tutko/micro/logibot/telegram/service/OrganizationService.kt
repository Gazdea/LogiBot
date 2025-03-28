package ru.tutko.micro.logibot.telegram.service

import ru.tutko.micro.logibot.telegram.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.dto.UserDto

interface OrganizationService {
    fun createOrganization(name: String, userId: Long): OrganizationDto?

    fun updateOrganization(organizationDto: OrganizationDto): OrganizationDto?

    fun deleteOrganization(id: Long)

    fun getOrganizationById(id: Long): OrganizationDto?

    fun getOrganizationsByUserId(userId: Long): MutableList<OrganizationDto>
}