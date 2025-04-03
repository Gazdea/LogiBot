package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermission
import ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermissionId

interface RoleTablePermissionRepository : JpaRepository<RoleTablePermission, RoleTablePermissionId>,
	JpaSpecificationExecutor<RoleTablePermission> {
}