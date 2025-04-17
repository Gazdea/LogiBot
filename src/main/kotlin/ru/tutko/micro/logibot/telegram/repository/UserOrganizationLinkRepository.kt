package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLink
import ru.tutko.micro.logibot.telegram.model.entity.UserOrganizationLinkId
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
}