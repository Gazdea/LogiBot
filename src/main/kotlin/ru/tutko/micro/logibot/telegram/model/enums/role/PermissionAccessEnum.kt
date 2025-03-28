package ru.tutko.micro.logibot.telegram.model.enums.role

enum class PermissionAccessEnum(val value: String) {
    MANAGE_ROLES("MANAGE_ROLES"),  // имеет право добавлять, удалять, редактировать роли
    MANAGE_EMPLOYEES("MANAGE_EMPLOYEES"),  // имеет право добавлять, удалять, редактировать сотрудников
    MANAGE_EMPLOYEE_ROLES("MANAGE_EMPLOYEE_ROLES"),  // имеет право добавлять, удалять, редактировать роли сотрудников
    MANAGE_TABLES("MANAGE_TABLES"),  // имеет право добавлять, удалять, редактировать таблицы
    VIEW_LOGS("VIEW_LOGS"),  // имеет право просматривать логи
    VIEW_EMPLOYEES("VIEW_EMPLOYEES"),  // имеет право просматривать сотрудников
    CREATE_REPORT("CREATE_REPORT")  // имеет право создавать отчёт
}