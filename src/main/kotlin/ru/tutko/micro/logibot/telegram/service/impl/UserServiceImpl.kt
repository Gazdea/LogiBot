package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.exception.BotException
import ru.tutko.micro.logibot.telegram.exception.NotFoundException
import ru.tutko.micro.logibot.telegram.mapper.UserMapper
import ru.tutko.micro.logibot.telegram.mapper.UserOrganizationLinkMapper
import ru.tutko.micro.logibot.telegram.model.data.UserOrganizationData
import ru.tutko.micro.logibot.telegram.model.dto.RoleOrganizationPermissionDto
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink
import ru.tutko.micro.logibot.telegram.model.enums.role.DefaultRoleEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.repository.OrganizationRepository
import ru.tutko.micro.logibot.telegram.repository.RoleRepository
import ru.tutko.micro.logibot.telegram.repository.UserOrganizationLinkRepository
import ru.tutko.micro.logibot.telegram.repository.UserRepository
import ru.tutko.micro.logibot.telegram.service.UserService
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val organizationRepository: OrganizationRepository,
    private val roleRepository: RoleRepository,
    private val userOrganizationLinkRepository: UserOrganizationLinkRepository,
    private val userOrganizationLinkMapper: UserOrganizationLinkMapper,
    private val userMapper: UserMapper,
): UserService {

    override fun getUserById(id: Long): UserDto {
        return userRepository.findById(id).map { userMapper.toDto(it) }.orElseThrow {
            throw NotFoundException("Пользователь не найден")
        }
    }

    @Transactional
    override fun getUserOrganizationLinkByUserOrganizationData(organizationId: Long, userId: Long): UserOrganizationLinkDto {
        return userOrganizationLinkRepository.findById_OrganizationIdAndId_UserId(organizationId, userId).map { userOrganizationLinkMapper.toDto(it) }.orElseThrow { NotFoundException("Пользователь или организация не найдены") }
    }

    override fun getUserByUserExternalId(userId: Long): UserDto {
        return userRepository.findByExternalUserId(userId).map { userMapper.toDto(it) }.orElseThrow {
            throw NotFoundException("Пользователь не найден")
        }
    }

    override fun getUserByUsername(username: String): Optional<UserDto> {
        return userRepository.findByUsername(username).map { userMapper.toDto(it) }
    }

    override fun createUser(user: UserDto): UserDto {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(user)))
    }

    override fun updateUser(user: UserDto): UserDto {

        return userMapper.toDto(userRepository.save(userMapper.toEntity(user)))
    }

    override fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    override fun createIfNotExists(user: UserDto): UserDto {
        val existingUser = userRepository.findByExternalUserId(user.externalUserId!!).orElse(null)

        return if(existingUser == null){
            userMapper.toDto(userRepository.save(userMapper.toEntity(user)))
        }else{
            userMapper.toDto(existingUser)
        }
    }

    override fun exists(userId: Long): Boolean {
        return userRepository.existsByExternalUserId(userId)
    }

    @Transactional
    override fun getUsersByOrganization(organizationId: Long, page: Int, size: Int): Page<UserDto> {
        val pageable = PageRequest.of(page, size)
        return userOrganizationLinkRepository.findById_OrganizationId(organizationId, pageable).map {
            it.user?.let { it1 -> userMapper.toDto(it1) }!!
        }
    }

    @Transactional
    override fun getUsersByOrganizationIdAndPermissions(
        organizationId: Long,
        permissions: Collection<PermissionAccessEnum>,
        include: Boolean,
        page: Int,
        size: Int
    ): Page<UserDto> {
        val pageable = PageRequest.of(page, size)
        val pageResult = if (include) {
            userOrganizationLinkRepository.findById_OrganizationIdAndRole_RoleOrganizationPermissions_PermissionIn(
                organizationId, permissions, pageable
            )
        } else {
            userOrganizationLinkRepository.findById_OrganizationIdAndRole_RoleOrganizationPermissions_PermissionNotIn(
                organizationId, permissions, pageable
            )
        }

        return pageResult.map { link -> userMapper.toDto(link.user!!) }
    }

    @Transactional
    override fun updateUserRoleByOrganization(organizationId: Long, userId: Long, roleId: Long){
        val userOrganizationLink = userOrganizationLinkRepository.findById_OrganizationIdAndId_UserId(organizationId, userId).orElseThrow { NotFoundException("Пользователь или организация не найдены") }
        val newRole = roleRepository.findRoleById(roleId)
        userOrganizationLinkRepository.updateRoleByOrganizationAndUser(newRole, userOrganizationLink.organization!!, userOrganizationLink.user!!)
    }

    @Transactional
    override fun joinUserOrganizationIfNeeded(organizationId: Long, userExternalId: Long): UserDto {
        val user = userRepository.findByExternalUserId(userExternalId).orElseThrow { NotFoundException("Пользователь не найден") }
        val organization = organizationRepository.findById(organizationId).orElseThrow { NotFoundException("Организация не найдена") }

        if (userOrganizationLinkRepository.existsById_UserIdAndId_OrganizationId(user.id!!, organizationId))
           throw BotException("Вы уже состоите в организации")

        val linkUserToOrganization = UserOrganizationLink(user, organization, organization.roles.find { it.roleName == DefaultRoleEnum.JOINER.value }!!)
        userOrganizationLinkRepository.save(linkUserToOrganization)

        return userMapper.toDto(user)
    }
}