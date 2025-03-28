package ru.tutko.micro.logibot.telegram.annotation.role

import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiresTableRolePermission(vararg val permissions: TablePermissionAccessEnum)
