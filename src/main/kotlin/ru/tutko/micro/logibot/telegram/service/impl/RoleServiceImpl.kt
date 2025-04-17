package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.exception.UserNotFoundException
import ru.tutko.micro.logibot.telegram.mapper.RoleMapper
import ru.tutko.micro.logibot.telegram.mapper.UserMapper
import ru.tutko.micro.logibot.telegram.mapper.UserOrganizationLinkMapper
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.repository.RoleRepository
import ru.tutko.micro.logibot.telegram.repository.UserOrganizationLinkRepository
import ru.tutko.micro.logibot.telegram.repository.UserRepository
import ru.tutko.micro.logibot.telegram.service.RoleService
import java.util.*

@Service
class RoleServiceImpl(
	private val roleRepository: RoleRepository,
	private val roleMapper: RoleMapper,
	private val userRepository: UserRepository,
	private val userMapper: UserMapper,

	private val userOrganizationLinkRepository: UserOrganizationLinkRepository,
	private val userOrganizationLinkMapper: UserOrganizationLinkMapper

): RoleService {
	@Transactional
	override fun getRole(roleId: Long): RoleDto {
		return roleMapper.toDto(roleRepository.findRoleById(roleId))
	}

	@Transactional
	override fun getRoleByUserOrganization(organizationId: Long, externalUserId: Long): RoleDto {
		val userOrganizationLink =
			userOrganizationLinkRepository.findById_OrganizationIdAndUser_ExternalUserId(organizationId, externalUserId)
				.orElseThrow { UserNotFoundException() }
		
		return roleMapper.toDto(userOrganizationLink.role!!)
	}


	override fun userExistsPermission(organizationId: Long, externalUserId: Long, permission: PermissionAccessEnum): Boolean {
		val user = userRepository.findByExternalUserId(externalUserId).orElseThrow { UserNotFoundException() }
		val roleInfo = roleRepository.findByOrganization_UserOrganizationLinks(
			userOrganizationLinkMapper.toEntity(
				UserOrganizationLinkDto(
					id = UserOrganizationLinkDto.UserOrganizationLinkIdDto(
						organizationId = organizationId,
						userId = user.id
					)
				)
			)
		).orElseThrow { UserNotFoundException("Не найдена связь пользователя с организацией") }

		return roleInfo.roleOrganizationPermissions.any {it.permission == permission || it.permission == PermissionAccessEnum.CREATOR}
	}

	override fun createRole(organizationId: Long, roleName: String): RoleDto {
		return roleMapper.toDto(
			roleRepository.save(
				roleMapper.toEntity(
					RoleDto(
						organization = RoleDto.OrganizationDto(id = organizationId),
						roleName = roleName
					)
				)
			)
		)
	}
}