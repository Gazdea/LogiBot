package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.dto.RoleOrganizationPermissionDto
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.exception.UserNotFoundException
import ru.tutko.micro.logibot.telegram.mapper.*
import ru.tutko.micro.logibot.telegram.model.entity.*
import ru.tutko.micro.logibot.telegram.model.enums.role.DefaultRoleEnum
import ru.tutko.micro.logibot.telegram.model.info.OrganizationInfo
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
        return roleRepository.saveAll(DefaultRoleEnum.getRolesDto(organizationId).map { roleMapper.toEntity(it) })
    }

//    private fun createPermissionForOrganization(organizationId: Long, role: Role) {
//        roleOrganizationPermissionRepository.saveAll(DefaultRoleEnum.getRolesDto(organizationId).map {
//            roleOrganizationPermissionMapper.toEntity(RoleOrganizationPermissionDto(
//                organization = RoleOrganizationPermissionDto.OrganizationDto1(organizationId),
//                role = RoleOrganizationPermissionDto.RoleDto(id = role.id),
//                permission = it
//            ))
//        })
//    }

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

    override fun getOrganizationById(id: Long): OrganizationInfo? {
        return organizationRepository.findInfoById(id).get()
    }

    override fun getOrganizationsByUserId(userId: Long): List<OrganizationInfo> {
        return organizationRepository.findByUserOrganizationLinks_User_ExternalUserId(userId)
    }

}
