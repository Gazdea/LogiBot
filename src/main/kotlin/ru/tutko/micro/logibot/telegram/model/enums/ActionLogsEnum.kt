package ru.tutko.micro.logibot.telegram.model.enums

enum class ActionLogsEnum(val value: String) {
    CREATE_TABLE("create_table"),
    EDIT_TABLE("edit_table"),
    DELETE_TABLE("delete_table"),

    ADD_COLUMNS("add_columns"),
    UPDATE_COLUMNS("update_columns"),
    DELETE_COLUMNS("delete_columns"),
    VIEW_COLUMNS("view_columns"),
}