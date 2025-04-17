package ru.tutko.micro.logibot.telegram.repository


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import java.util.*

interface OrganizationRepository : JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

	@EntityGraph(attributePaths = ["userOrganizationLinks", "chats", "dataTables", "roles", "roleOrganizationPermissions", "userOrganizationLinks"])
	fun findByUserOrganizationLinks_User_ExternalUserId(externalUserId: Long, pageable: Pageable): Page<Organization>

}
