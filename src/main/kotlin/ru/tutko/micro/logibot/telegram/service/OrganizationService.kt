package ru.tutko.micro.logibot.telegram.service

import org.springframework.data.domain.Page
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.entity.Organization

interface OrganizationService {
    fun createOrganization(name: String, userId: Long): OrganizationDto?

    fun updateOrganization(organizationDto: OrganizationDto): OrganizationDto?

    fun deleteOrganization(id: Long)

    fun getOrganizationById(id: Long): OrganizationDto

    fun getOrganizationsByUserId(userId: Long, page: Int, size: Int = 8): Page<OrganizationDto>

    fun getRolesOrganization(organizationId: Long, page: Int, size: Int = 8): Page<RoleDto>

}