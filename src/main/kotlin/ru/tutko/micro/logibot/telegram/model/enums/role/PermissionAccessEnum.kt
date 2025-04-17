package ru.tutko.micro.logibot.telegram.model.enums.role

enum class PermissionAccessEnum() {
    CREATOR, // Имеет полные права

    SETTINGS, // имеет право изменять настройки организации

    VIEW_ROLES, // Просмотр списка ролей
    MANAGE_ROLES,  // имеет право добавлять, удалять, редактировать роли
    MANAGE_ROLE_TABLE_PERMISSIONS, // имеет право изменять доступы к таблицам для роли
    MANAGE_ROLE_ORGANIZATION_PERMISSIONS, // имеет право изменять доступы к организации

    VIEW_EMPLOYEES,  // имеет право просматривать сотрудников
    MANAGE_EMPLOYEES,  // имеет право добавлять, удалять, редактировать сотрудников
    MANAGE_EMPLOYEE_ROLES,  // имеет право добавлять, удалять, редактировать роли сотрудников

    VIEW_TABLES, // имеет право просматривать все таблицы
    MANAGE_TABLES,  // имеет право добавлять, удалять, редактировать таблицы

    VIEW_LOGS,  // имеет право просматривать логи

    CREATE_REPORT,  // имеет право создавать отчёт
}