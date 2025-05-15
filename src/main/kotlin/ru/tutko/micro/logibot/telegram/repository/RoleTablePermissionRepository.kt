package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.DataTable
import ru.tutko.micro.logibot.telegram.model.entity.Role
import ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermission
import ru.tutko.micro.logibot.telegram.model.entity.RoleTablePermissionId
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum
import java.util.Optional

interface RoleTablePermissionRepository : JpaRepository<RoleTablePermission, RoleTablePermissionId>,
	JpaSpecificationExecutor<RoleTablePermission> {


	fun findByRoleAndTableAndPermission(
		role: Role,
		table: DataTable,
		permission: TablePermissionAccessEnum
	): RoleTablePermission?
}