package ru.tutko.micro.logibot.telegram.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import ru.tutko.micro.logibot.telegram.annotation.role.RequiresRolePermission
import ru.tutko.micro.logibot.telegram.annotation.role.RequiresTableRolePermission

@Aspect
@Component
class PermissionAccessCheckAspect(
	// TODO: Подключить RolePermissionService
	// private val rolePermissionService: RolePermissionService,
	// TODO: Подключить TableRole
) {
	@Around("@annotation(requiresRolePermissionAnnotation)")
	fun checkPermissionRole(joinPoint: ProceedingJoinPoint, requiresRolePermissionAnnotation: RequiresRolePermission): Any? {
		// TODO: Реализовать проверку ролей через rolePermissionService
		return joinPoint.proceed()
	}

	@Around("@annotation(requiresTableRolePermissionAnnotation)")
	fun checkTablePermissionRole(joinPoint: ProceedingJoinPoint, requiresTableRolePermissionAnnotation: RequiresTableRolePermission): Any? {
		// TODO: Реализовать проверку ролей через rolePermissionService
		return joinPoint.proceed()
	}
}