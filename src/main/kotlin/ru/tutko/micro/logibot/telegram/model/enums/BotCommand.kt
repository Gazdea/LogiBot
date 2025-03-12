package ru.tutko.micro.logibot.telegram.model.enums

enum class BotCommand(val value: String) {
    START("/start"),
    TEST("/test"),
    HELP("/help"),
    SETTINGS("/settings"),
    CANCEL("/cancel"),
}