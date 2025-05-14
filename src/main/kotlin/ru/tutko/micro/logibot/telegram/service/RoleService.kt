package ru.tutko.micro.logibot.telegram.service

import ru.tutko.micro.logibot.telegram.model.dto.RoleDto
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

interface RoleService {

	/**
	 * Возвращает информацию о роли по её идентификатору.
	 *
	 * @param roleId ID роли.
	 * @return DTO роли.
	 */
	fun getRole(roleId: Long): RoleDto

	/**
	 * Возвращает роль пользователя в рамках конкретной организации.
	 *
	 * @param organizationId ID организации.
	 * @param externalUserId ID пользователя (возможно, Telegram ID или внешний ID).
	 * @return DTO роли пользователя.
	 */
	fun getRoleByUserOrganization(organizationId: Long, externalUserId: Long): RoleDto

	/**
	 * Проверяет, обладает ли пользователь заданным правом доступа в рамках организации.
	 *
	 * @param organizationId ID организации.
	 * @param externalUserId ID пользователя.
	 * @param permission Проверяемое разрешение.
	 * @return true — если разрешение у пользователя есть, иначе false.
	 */
	fun userExistsPermission(
		organizationId: Long,
		externalUserId: Long,
		permission: PermissionAccessEnum
	): Boolean

	/**
	 * Создаёт новую роль в рамках конкретной организации.
	 *
	 * @param organizationId ID организации.
	 * @param roleName Название роли.
	 * @return DTO созданной роли.
	 */
	fun createRole(organizationId: Long, roleName: String): RoleDto

	/**
	 * Возвращает список всех ролей, существующих в указанной организации.
	 *
	 * @param organizationId ID организации.
	 * @return Список DTO ролей.
	 */
	fun getRolesByOrganizationId(organizationId: Long): List<RoleDto>

	/**
	 * Обновляет (назначает или снимает) разрешение у роли.
	 *
	 * @param roleId ID роли.
	 * @param organizationId ID организации (для валидации, что роль принадлежит организации).
	 * @param permission Разрешение, которое нужно назначить или снять.
	 * @return true — если операция прошла успешно, иначе false.
	 */
	fun updateRolePermission(
		roleId: Long,
		organizationId: Long,
		permission: PermissionAccessEnum
	): Boolean
}
