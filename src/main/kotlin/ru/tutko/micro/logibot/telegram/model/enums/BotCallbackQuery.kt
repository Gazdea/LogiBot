package ru.tutko.micro.logibot.telegram.model.enums

enum class BotCallbackQuery(val value: String) {
    SETTINGS("settings_"),
    TEST_INPUT("test_input"),
    TEST_MAP("test_map"),
    TEST_CLOSE("test_close"),
    CANCEL("cancel_wait_for_input")
}