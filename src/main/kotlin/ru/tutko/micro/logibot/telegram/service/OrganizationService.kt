package ru.tutko.micro.logibot.telegram.service

import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto

interface OrganizationService {
    fun createOrganization(name: String): OrganizationDto?
    fun updateOrganization(organizationDto: OrganizationDto): OrganizationDto?
    fun deleteOrganization(id: Long)
    fun getOrganizationById(id: Long): OrganizationDto?
    fun getOrganizations(): List<OrganizationDto>
}