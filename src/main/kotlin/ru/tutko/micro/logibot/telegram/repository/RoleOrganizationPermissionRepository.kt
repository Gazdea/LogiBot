package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.Organization
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission
import ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermissionId
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

interface RoleOrganizationPermissionRepository :
	JpaRepository<RoleOrganizationPermission, RoleOrganizationPermissionId>,
	JpaSpecificationExecutor<RoleOrganizationPermission> {

	fun findByRoleAndOrganizationAndPermission(
		role: Role,
		organization: Organization,
		permission: PermissionAccessEnum
	): RoleOrganizationPermission?

}