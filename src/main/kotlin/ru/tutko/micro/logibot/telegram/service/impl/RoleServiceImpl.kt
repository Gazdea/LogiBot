package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.exception.NotFoundException
import ru.tutko.micro.logibot.telegram.exception.UserNotFoundException
import ru.tutko.micro.logibot.telegram.mapper.OrganizationMapper
import ru.tutko.micro.logibot.telegram.mapper.RoleMapper
import ru.tutko.micro.logibot.telegram.mapper.RoleOrganizationPermissionMapper
import ru.tutko.micro.logibot.telegram.mapper.RoleTablePermissionMapper
import ru.tutko.micro.logibot.telegram.mapper.UserMapper
import ru.tutko.micro.logibot.telegram.mapper.UserOrganizationLinkMapper
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission
import ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermissionId
import ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermission
import ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermissionId
import ru.tutko.micro.logibot.telegram.model.entity.User
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum
import ru.tutko.micro.logibot.telegram.repository.DataTableRepository
import ru.tutko.micro.logibot.telegram.repository.OrganizationRepository
import ru.tutko.micro.logibot.telegram.repository.RoleOrganizationPermissionRepository
import ru.tutko.micro.logibot.telegram.repository.RoleRepository
import ru.tutko.micro.logibot.telegram.repository.RoleTablePermissionRepository
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

	private val roleOrganizationPermissionRepository: RoleOrganizationPermissionRepository,
	private val roleOrganizationPermissionMapper: RoleOrganizationPermissionMapper,

	private val roleTablePermissionRepository: RoleTablePermissionRepository,
	private val roleTablePermissionMapper: RoleTablePermissionMapper,

	private val organizationRepository: OrganizationRepository,
	private val organizationMapper: OrganizationMapper,

	private val userOrganizationLinkRepository: UserOrganizationLinkRepository,
	private val userOrganizationLinkMapper: UserOrganizationLinkMapper,

	private val dataTableRepository: DataTableRepository,

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


	override fun userExistsPermission(
		organizationId: Long,
		externalUserId: Long,
		permission: PermissionAccessEnum
	): Boolean {
		val user = getUserByExternalId(externalUserId)
		val role = getUserRoleInOrganization(user.id!!, organizationId)

		return role.roleOrganizationPermissions.any {
			it.permission == permission || it.permission == PermissionAccessEnum.CREATOR
		}
	}

	@Transactional
	override fun userExistsTablePermission(
		tableId: Long,
		externalUserId: Long,
		tablePermission: TablePermissionAccessEnum
	): Boolean {
		val user = getUserByExternalId(externalUserId)
		val table = dataTableRepository.findById(tableId).orElseThrow { NotFoundException("Таблица не найдена") }
		val role = getUserRoleInOrganization(user.id!!, table.organization!!.id!!)

		return role.roleOrganizationPermissions.any { it.permission == PermissionAccessEnum.CREATOR } ||
				role.roleTablePermissions.any {
					it.permission == TablePermissionAccessEnum.CREATOR || it.permission == tablePermission
				}
	}

	private fun getUserByExternalId(externalUserId: Long): User {
		return userRepository.findByExternalUserId(externalUserId)
			.orElseThrow { UserNotFoundException() }
	}

	private fun getUserRoleInOrganization(userId: Long, organizationId: Long): Role {
		val userLink = userOrganizationLinkMapper.toEntity(
			UserOrganizationLinkDto(
				id = UserOrganizationLinkDto.UserOrganizationLinkIdDto(
					userId = userId,
					organizationId = organizationId
				)
			)
		)
		return roleRepository.findByOrganization_UserOrganizationLinks(userLink)
			.orElseThrow { UserNotFoundException("Не найдена связь пользователя с организацией") }
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

	override fun getRolesByOrganizationId(organizationId: Long): List<RoleDto> {
		return roleRepository.getRolesByOrganizationId(organizationId).map { role -> roleMapper.toDto(role) }
	}

	@Override
	@Transactional
	override fun updateRolePermission(
		roleId: Long,
		organizationId: Long,
		permission: PermissionAccessEnum
	): Boolean {
		val role = roleRepository.findById(roleId).orElseThrow { NotFoundException("Роль не найдена") }
		val organization = organizationRepository.findById(organizationId).orElseThrow { NotFoundException("Организация не найдена") }

		val existing = roleOrganizationPermissionRepository.findByRoleAndOrganizationAndPermission(
			role, organization, permission
		)

		return if (existing != null) {
			roleOrganizationPermissionRepository.delete(existing)
			false
		} else {
			val entity = RoleOrganizationPermission().apply {
				this.id = RoleOrganizationPermissionId().apply {
					this.organizationId = organization.id
					this.roleId = role.id
					this.permission = permission
				}
				this.role = role
				this.organization = organization
				this.permission = permission
			}
			roleOrganizationPermissionRepository.save(entity)
			true
		}
	}

	@Transactional
	override fun updateRolePermissionByTableId(
		roleId: Long,
		tableId: Long,
		permission: TablePermissionAccessEnum
	): Boolean {
		val role = roleRepository.findById(roleId).orElseThrow { NotFoundException("Роль не найдена") }
		val table = dataTableRepository.findById(tableId).orElseThrow { NotFoundException("Таблица не найдена") }

		val existing = roleTablePermissionRepository.findByRoleAndTableAndPermission(
			role, table, permission
		)

		return if (existing != null) {
			roleTablePermissionRepository.delete(existing)
			false
		} else {
			val entity = RoleTablePermission().apply {
				this.id = RoleTablePermissionId().apply {
					this.tableId = table.id
					this.roleId = role.id
					this.permission = permission
				}
				this.role = role
				this.table = table
				this.permission = permission
			}
			roleTablePermissionRepository.save(entity)
			true
		}
	}

}