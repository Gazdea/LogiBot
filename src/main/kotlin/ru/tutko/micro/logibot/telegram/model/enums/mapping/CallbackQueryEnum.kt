package ru.tutko.micro.logibot.telegram.model.enums.mapping

enum class CallbackQueryEnum(val value: String) {

    SETTINGS("settings"),
    TEST_INPUT("testInput"),
    TEST_MAP("testMap"),
    TEST_CLOSE("testClose"),
    CANCEL("cancelWaitForInput"),
    CREATE_ORGANIZATION("createOrganization"),

    START_GET_ORGANIZATIONS("startGetOrganizations"),

    BOT_JOIN_ADD_ORGANIZATION("botJoinAddOrganization"),

    GET_ORGANIZATION("getOrganization"),
}