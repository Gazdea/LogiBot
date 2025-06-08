package ru.tutko.micro.logibot.telegram.model.enums.mapping

import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum

enum class CallbackQueryEnum(val value: String, val permission: PermissionAccessEnum? = null, val tablePermissionAccessEnum: TablePermissionAccessEnum? = null) {

    TEST_INPUT("testInput"),
    TEST_MAP("testMap"),
    TEST_CLOSE("testClose"),

    SETTINGS("settings"),

    PAGINATE_COMMAND_GET_TABLE("paginateCommandGetTable"),

    CANCEL("cancelWaitForInput"),
    CREATE_ORGANIZATION("createOrganization"),

    BOT_JOIN_ADD_ORGANIZATION("botJoinAddOrganization", PermissionAccessEnum.CREATOR),

    CREATE_JOIN_REQUEST("createJoinRequest"),

    GET_ORGANIZATION("getOrganization"),

    SET_CHAT_ORGANIZATION("setChatOrganization", PermissionAccessEnum.CREATOR),
    SET_CHAT_CREATE_ORGANIZATION("setChatCreateOrganization", PermissionAccessEnum.CREATOR),

    PAGINATE_ORGANIZATIONS("paginateOrganizations"),

    PAGINATE_SET_CHAT_ORGANIZATION("paginateSetChatOrganization", PermissionAccessEnum.CREATOR),

    PAGINATE_GET_ROLES("paginateGetRoles", PermissionAccessEnum.VIEW_ROLES),

    GET_ROLE("getRole", PermissionAccessEnum.VIEW_ROLES),

    MANAGE_USER_ROLE("manageUserRole", PermissionAccessEnum.MANAGE_EMPLOYEE_ROLES),

    CREATE_ROLE("createRole", PermissionAccessEnum.MANAGE_ROLES),

    PAGINATE_GET_USERS("paginateGetUsers", PermissionAccessEnum.VIEW_EMPLOYEES),

    PAGINATE_GET_TABLES("paginateGetTables", PermissionAccessEnum.VIEW_TABLES),

    PAGINATE_GET_CHATS("paginateGetChats", PermissionAccessEnum.SETTINGS),

    LOGS_ORGANIZATION("logsOrganization", PermissionAccessEnum.VIEW_LOGS),

    REPORT_ORGANIZATION("reportOrganization", PermissionAccessEnum.CREATE_REPORT),

    GET_USER("getUser", PermissionAccessEnum.VIEW_EMPLOYEES),

    PAGINATE_GET_JOINERS("paginateGetJoiners", PermissionAccessEnum.MANAGE_EMPLOYEES),

    SET_ROLE_USER("setRoleUser", PermissionAccessEnum.MANAGE_EMPLOYEE_ROLES),

    UPDATE_ROLE_PERMISSION("updateRolePermission", PermissionAccessEnum.MANAGE_ROLES),

    GET_TABLE("getTables", PermissionAccessEnum.VIEW_TABLES, TablePermissionAccessEnum.VIEW_TABLE),

    CREATE_TABLE("createTable", PermissionAccessEnum.MANAGE_TABLES),

    GET_DATA_TABLE_COLUMN("getDataTableColumn", PermissionAccessEnum.VIEW_TABLES, TablePermissionAccessEnum.VIEW_METADATA),

    ADD_DATA_TABLE_COLUMN("addDataTableColumn", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.FILL_METADATA),

    EDIT_TABLE_COLUMN("editTableColumn", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.EDIT_TABLE),

    MANAGE_TABLE_PERMISSIONS("editTableRoles", PermissionAccessEnum.MANAGE_ROLES, TablePermissionAccessEnum.MANAGE_TABLE_PERMISSIONS),

    GET_REPORT_TABLE("getReportTable", PermissionAccessEnum.CREATE_REPORT, TablePermissionAccessEnum.CREATE_REPORT),

    ADD_COLUMN_TABLE("addColumnTable", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.EDIT_TABLE),
    SET_NAME_COLUMN("setNameColumn", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.EDIT_TABLE),

    GET_COLUMN_TABLE("getColumnTable", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.EDIT_TABLE),

    DELETE_COLUMN("deleteColumn", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.EDIT_TABLE),

    GET_TABLE_ROLE("getTableRole", PermissionAccessEnum.MANAGE_ROLES),

    ADD_METADATA_COLUMN("addMetadataColumn", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.FILL_METADATA),

    SAVE_METADATA("saveMetadata", PermissionAccessEnum.MANAGE_TABLES, TablePermissionAccessEnum.FILL_METADATA),

    GET_DATA_TABLE("getDataTable", PermissionAccessEnum.MANAGE_ROLES, TablePermissionAccessEnum.VIEW_METADATA),

}