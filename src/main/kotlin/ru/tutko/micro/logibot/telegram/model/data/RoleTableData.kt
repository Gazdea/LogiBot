package ru.tutko.micro.logibot.telegram.model.data

import kotlinx.serialization.Serializable
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum

@Serializable
data class RoleTableData(
	val organizationId: Long,
	val roleId: Long,
	val tableId: Long,
	val updateTablePermission: TablePermissionAccessEnum? = null,
	val pageable: Pageable = Pageable(),
	): Payload()