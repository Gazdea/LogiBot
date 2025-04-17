package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLinkId
import java.util.*

interface RoleRepository : JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
	fun getRolesByOrganizationId(organizationId: Long): List<Role>

	fun findByOrganization_UserOrganizationLinks_Id(id: UserOrganizationLinkId): Optional<Role>


	fun findByOrganization_Id(id: Long, pageable: Pageable): Page<Role>

	fun findRoleById(id: Long): Role

	fun findByOrganization_UserOrganizationLinks(userOrganizationLinks: UserOrganizationLink): Optional<Role>
}