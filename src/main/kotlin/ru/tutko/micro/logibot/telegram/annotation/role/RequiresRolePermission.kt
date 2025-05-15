package ru.tutko.micro.logibot.telegram.annotation.role

import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresRolePermission(vararg val permissions: PermissionAccessEnum)
