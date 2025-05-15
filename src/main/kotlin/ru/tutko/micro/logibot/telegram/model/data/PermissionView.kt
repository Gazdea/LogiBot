package ru.tutko.micro.logibot.telegram.model.data

import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

data class PermissionView(
	val permission: PermissionAccessEnum,
	val nameRu: String,
	val hasPermission: Boolean
)
