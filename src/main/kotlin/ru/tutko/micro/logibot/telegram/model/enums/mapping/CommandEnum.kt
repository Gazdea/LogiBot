package ru.tutko.micro.logibot.telegram.model.enums.mapping

enum class CommandEnum(val value: String) {
    TEST_EXCEPTION("/test_exception"),
    CREATE_ORGANIZATION("/create_organization"),
    START("/start"),
    TEST("/test"),
    HELP("/help"),
    SETTINGS("/settings"),
    CANCEL("/cancel");

}