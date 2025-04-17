package ru.tutko.micro.logibot.telegram.model.enums.mapping

import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

enum class CallbackQueryEnum(val value: String, val permission: PermissionAccessEnum? = null) {

    TEST_INPUT("testInput"),
    TEST_MAP("testMap"),
    TEST_CLOSE("testClose"),

    SETTINGS("settings"),

    CANCEL("cancelWaitForInput"),
    CREATE_ORGANIZATION("createOrganization"),

    BOT_JOIN_ADD_ORGANIZATION("botJoinAddOrganization", PermissionAccessEnum.CREATOR),

    GET_ORGANIZATION("getOrganization"),

    SET_CHAT_ORGANIZATION("setChatOrganization", PermissionAccessEnum.CREATOR),
    SET_CHAT_CREATE_ORGANIZATION("setChatCreateOrganization", PermissionAccessEnum.CREATOR),

    PAGINATE_ORGANIZATIONS("paginateOrganizations"),

    PAGINATE_SET_CHAT_ORGANIZATION("paginateSetChatOrganization", PermissionAccessEnum.CREATOR),

    PAGINATE_GET_ROLES("paginateGetRoles", PermissionAccessEnum.VIEW_ROLES),

    GET_ROLE("getRole", PermissionAccessEnum.VIEW_ROLES),

    CREATE_ROLE("createRole", PermissionAccessEnum.MANAGE_ROLES),

    PAGINATE_GET_USERS("paginateGetUsers", PermissionAccessEnum.VIEW_EMPLOYEES),

    PAGINATE_GET_TABLES("paginateGetTables", PermissionAccessEnum.VIEW_TABLES),

    PAGINATE_GET_CHATS("paginateGetChats", PermissionAccessEnum.SETTINGS),

    LOGS_ORGANIZATION("logsOrganization", PermissionAccessEnum.VIEW_LOGS),

    REPORT_ORGANIZATION("reportOrganization", PermissionAccessEnum.CREATE_REPORT),

    GET_USER("getUser", PermissionAccessEnum.VIEW_EMPLOYEES),
}