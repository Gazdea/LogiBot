package ru.tutko.micro.logibot.telegram.model.enums.role

enum class PermissionAccessEnum(val nameRu: String, val isVisible: Boolean = true) {
    CREATOR("Создатель", false), // Имеет полные права

    SETTINGS("Доступ к настройкам организации"), // имеет право изменять настройки организации

    VIEW_ROLES("Просмотр списка ролей"), // Просмотр списка ролей
    MANAGE_ROLES("Управление ролями"),  // имеет право добавлять, удалять, редактировать роли
    MANAGE_ROLE_TABLE_PERMISSIONS("Управление доступами к таблицам для роли"), // имеет право изменять доступы к таблицам для роли
    MANAGE_ROLE_ORGANIZATION_PERMISSIONS("Управление доступами к организации"), // имеет право изменять доступы к организации

    VIEW_EMPLOYEES("Просмотр сотрудников"),  // имеет право просматривать сотрудников
    MANAGE_EMPLOYEES("Управление сотрудниками"),  // имеет право добавлять, удалять, редактировать сотрудников
    MANAGE_EMPLOYEE_ROLES("Управление ролями сотрудников"),  // имеет право добавлять, удалять, редактировать роли сотрудников

    VIEW_TABLES("Просмотр таблиц"), // имеет право просматривать все таблицы
    MANAGE_TABLES("Управление таблицами"),  // имеет право добавлять, удалять, редактировать таблицы
    VIEW_LOGS("Просмотр логов"),  // имеет право просматривать логи
    CREATE_REPORT("Создание отчётов"),  // имеет право создавать отчёт

    JOINER("Приглашенный"); // Пользователь сделал запрос на присоединение

    companion object {
        private val implications: Map<PermissionAccessEnum, Set<PermissionAccessEnum>> = mapOf(
            MANAGE_ROLES to setOf(VIEW_ROLES),
            MANAGE_ROLE_TABLE_PERMISSIONS to setOf(VIEW_ROLES),
            MANAGE_ROLE_ORGANIZATION_PERMISSIONS to setOf(VIEW_ROLES),

            MANAGE_EMPLOYEES to setOf(VIEW_EMPLOYEES),
            MANAGE_EMPLOYEE_ROLES to setOf(VIEW_EMPLOYEES),

            MANAGE_TABLES to setOf(VIEW_TABLES),
            VIEW_LOGS to setOf(VIEW_TABLES),
            CREATE_REPORT to setOf(VIEW_TABLES),

            CREATOR to entries.toSet()
        )

        fun normalizePermissions(permissions: Set<PermissionAccessEnum>): Set<PermissionAccessEnum> {
            val result = permissions.toMutableSet()
            val toProcess = ArrayDeque(permissions)

            while (toProcess.isNotEmpty()) {
                val current = toProcess.removeFirst()
                val implied = implications[current].orEmpty()
                for (permission in implied) {
                    if (result.add(permission)) {
                        toProcess.add(permission)
                    }
                }
            }

            return result
        }
    }
}
