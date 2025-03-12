package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import ru.tutko.micro.logibot.telegram.mapper.OrganizationMapper
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.repository.OrganizationRepository
import ru.tutko.micro.logibot.telegram.service.OrganizationService

@Service
class OrganizationServiceImpl(
    private val organizationRepository: OrganizationRepository,
    private val organizationMapper: OrganizationMapper
): OrganizationService {

    override fun createOrganization(name: String): OrganizationDto? {
        val organization = organizationRepository.save(Organization().apply { this.name = name })
        val organizationDto = organizationMapper.toDto(organization)
        return organizationDto
    }

    override fun updateOrganization(organizationDto: OrganizationDto): OrganizationDto? {
        return organizationMapper.toDto(organizationRepository.save(Organization().apply { this.name = organizationDto.name }))
    }

    override fun deleteOrganization(id: Long) {
        return organizationRepository.deleteById(id);
    }

    override fun getOrganizationById(id: Long): OrganizationDto? {
        return organizationMapper.toDto(organizationRepository.findById(id).get())
    }

    override fun getOrganizations(): List<OrganizationDto> {
        return organizationRepository.findAll().stream().map { organization -> organizationMapper.toDto(organization) }.toList()
    }

}
