package ru.tutko.micro.logibot.telegram.model.enums.role

enum class PermissionAccessEnum() {
    MANAGE_ROLES,  // имеет право добавлять, удалять, редактировать роли
    MANAGE_EMPLOYEES,  // имеет право добавлять, удалять, редактировать сотрудников
    MANAGE_EMPLOYEE_ROLES,  // имеет право добавлять, удалять, редактировать роли сотрудников
    MANAGE_TABLES,  // имеет право добавлять, удалять, редактировать таблицы
    VIEW_LOGS,  // имеет право просматривать логи
    VIEW_EMPLOYEES,  // имеет право просматривать сотрудников
    CREATE_REPORT,  // имеет право создавать отчёт

    NULL;
}