package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.model.entity.User
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLinkId
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import java.util.*

interface UserOrganizationLinkRepository : JpaRepository<UserOrganizationLink, UserOrganizationLinkId>,
	JpaSpecificationExecutor<UserOrganizationLink> {

	fun findByUser_ExternalUserId(externalUserId: Long): Optional<UserOrganizationLink>

	fun findByUserIdAndOrganizationId(userId: Long, organizationId: Long): Optional<UserOrganizationLink>


	fun findById_OrganizationIdAndUser_ExternalUserId(
		organizationId: Long,
		externalUserId: Long
	): Optional<UserOrganizationLink>



	fun findById_OrganizationId(organizationId: Long, pageable: Pageable): Page<UserOrganizationLink>


	fun findById_OrganizationIdAndRole_RoleOrganizationPermissions_PermissionIn(
		organizationId: Long,
		permissions: Collection<PermissionAccessEnum>,
		pageable: Pageable
	): Page<UserOrganizationLink>


	fun findById_OrganizationIdAndRole_RoleOrganizationPermissions_PermissionNotIn(
		organizationId: Long,
		permissions: Collection<PermissionAccessEnum>,
		pageable: Pageable
	): Page<UserOrganizationLink>


	fun existsById_UserIdAndId_OrganizationId(userId: Long, organizationId: Long): Boolean


	fun findById_OrganizationIdAndId_UserId(organizationId: Long, userId: Long): Optional<UserOrganizationLink>


	@Transactional
	@Modifying
	@Query("update UserOrganizationLink u set u.role = ?1 where u.organization = ?2 and u.user = ?3")
	fun updateRoleByOrganizationAndUser(role: Role, organization: Organization, user: User)
}