package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermission
import ru.tutko.micro.logibot.telegram.model.entity.RoleOrganizationPermissionId

interface RoleOrganizationPermissionRepository :
	JpaRepository<RoleOrganizationPermission, RoleOrganizationPermissionId>,
	JpaSpecificationExecutor<RoleOrganizationPermission> {

}