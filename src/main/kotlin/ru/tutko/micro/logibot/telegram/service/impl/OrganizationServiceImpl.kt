package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import ru.tutko.micro.logibot.telegram.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.dto.UserOrganizationLinkIdDto
import ru.tutko.micro.logibot.telegram.exception.UserNotFoundException
import ru.tutko.micro.logibot.telegram.mapper.OrganizationMapper
import ru.tutko.micro.logibot.telegram.mapper.RoleMapper
import ru.tutko.micro.logibot.telegram.mapper.UserMapper
import ru.tutko.micro.logibot.telegram.mapper.UserOrganizationLinkMapper
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.model.entity.User
import ru.tutko.micro.logibot.telegram.model.enums.role.DefaultRoleEnum
import ru.tutko.micro.logibot.telegram.repository.*
import ru.tutko.micro.logibot.telegram.service.OrganizationService

@Service
class OrganizationServiceImpl(
    private val organizationRepository: OrganizationRepository,
    private val organizationMapper: OrganizationMapper,

    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val roleRepository: RoleRepository,
    private val roleMapper: RoleMapper,
    private val userOrganizationLinkRepository: UserOrganizationLinkRepository,
    private val userOrganizationLinkMapper: UserOrganizationLinkMapper
): OrganizationService {

    override fun createOrganization(name: String, userId: Long): OrganizationDto {
        val organization = createOrganization(name)
        val user = findUserById(userId)
        val roles = createRolesForOrganization(organization.id!!)
        linkUserToOrganization(user, organization, roles)

        return organizationMapper.toDto(organization)
    }

    private fun createOrganization(name: String): Organization {
        return organizationRepository.save(Organization().apply { this.name = name })
    }

    private fun findUserById(userId: Long): User {
        return userRepository.findByUserId(userId).orElseThrow { UserNotFoundException("Пользователь $userId не найден") }
    }

    private fun createRolesForOrganization(organizationId: Long): List<Role> {
        return roleRepository.saveAll(DefaultRoleEnum.getRolesDto(organizationId).map { roleMapper.toEntity(it) })
    }

    private fun linkUserToOrganization(user: User, organization: Organization, roles: List<Role>) {
        val linkUserToOrganization = userOrganizationLinkMapper.toEntity(
            UserOrganizationLinkDto(
                id = UserOrganizationLinkIdDto(
                    userId = user.id,
                    organizationId = organization.id
                ),
                user = userMapper.toDto(user),
                organization = organizationMapper.toDto(organization),
                roleId = roles.first { role -> role.role == DefaultRoleEnum.ADMIN.value }.id
            )
        )
        userOrganizationLinkRepository.save(linkUserToOrganization)
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

    override fun getOrganizationsByUserId(userId: Long): MutableList<OrganizationDto> {
        return organizationRepository.findByUserOrganizationLinks_User_UserId(userId).map { organizationMapper.toDto(it) }.toMutableList()
    }

}
