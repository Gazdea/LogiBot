package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.exception.NotFoundException
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.dto.RoleOrganizationPermissionDto
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.exception.UserNotFoundException
import ru.tutko.micro.logibot.telegram.mapper.*
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.entity.*
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
    private val userOrganizationLinkMapper: UserOrganizationLinkMapper,
    private val roleOrganizationPermissionRepository: RoleOrganizationPermissionRepository,
    private val roleOrganizationPermissionMapper: RoleOrganizationPermissionMapper,

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
        return userRepository.findByExternalUserId(userId).orElseThrow { UserNotFoundException("Пользователь $userId не найден") }
    }

    private fun createRolesForOrganization(organizationId: Long): List<Role> {
        DefaultRoleEnum.entries.forEach { defaultRole ->
            val role = roleRepository.save(
                roleMapper.toEntity(RoleDto(
                    roleName = defaultRole.value,
                    organization = RoleDto.OrganizationDto(id = organizationId)
                ))
            )

            val permissions = defaultRole.permissionsAccessEnum.orEmpty().map { permissionEnum ->
                roleOrganizationPermissionMapper.toEntity(
                    RoleOrganizationPermissionDto(
                        id = RoleOrganizationPermissionDto.RoleOrganizationPermissionIdDto(
                            organizationId = organizationId,
                            roleId = role.id
                        ),
                        organization = RoleOrganizationPermissionDto.OrganizationDto1(organizationId),
                        role = RoleOrganizationPermissionDto.RoleDto(id = role.id),
                        permission = permissionEnum
                    )
                )
            }
            roleOrganizationPermissionRepository.saveAll(permissions)
        }
        return roleRepository.getRolesByOrganizationId(organizationId)
    }

    private fun linkUserToOrganization(user: User, organization: Organization, roles: List<Role>) {
        val adminRole = roles.first { it.roleName == DefaultRoleEnum.ADMIN.value }

        val linkUserToOrganization = UserOrganizationLink(user,organization, adminRole)
        userOrganizationLinkRepository.save(linkUserToOrganization)
    }

    override fun updateOrganization(organizationDto: OrganizationDto): OrganizationDto? {
        return organizationMapper.toDto(organizationRepository.save(Organization().apply { this.name = organizationDto.name }))
    }

    override fun deleteOrganization(id: Long) {
        return organizationRepository.deleteById(id);
    }

    @Transactional
    override fun getOrganizationById(id: Long): OrganizationDto {
        return organizationMapper.toDto(organizationRepository.findById(id).orElseThrow {NotFoundException()})
    }

    override fun getOrganizationsByUserId(userId: Long, page: Int, size: Int): Page<OrganizationDto> {
        val pageable = PageRequest.of(page, size)
        val organizations = organizationRepository.findByUserOrganizationLinks_User_ExternalUserId(userId, pageable)

        return organizations.map { organizationMapper.toDto(it) }
    }

    @Transactional
    override fun getRolesOrganization(organizationId: Long, page: Int, size: Int): Page<RoleDto> {
        val pageable = PageRequest.of(page, size)
        val role = roleRepository.findByOrganization_Id(organizationId, pageable)
        return role.map { roleMapper.toDto(it) }
    }

}
