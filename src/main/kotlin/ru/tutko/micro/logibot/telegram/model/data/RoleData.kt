package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

@Serializable
data class RoleData(
	val orgId: Long,
	val roleId: Long,
	val updatePermission: PermissionAccessEnum? = null,
	val pageablePermission: Pageable = Pageable(),
): Payload()