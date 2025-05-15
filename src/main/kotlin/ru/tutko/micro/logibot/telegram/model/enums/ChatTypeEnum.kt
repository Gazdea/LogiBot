package ru.tutko.micro.logibot.telegram.model.enums

enum class ChatTypeEnum(val value: String) {
    PRIVATE("private"),
    GROUP("group"),
    SUPERGROUP("supergroup"),
    CHANNEL("channel");

    companion object {
        fun fromValue(value: String): ChatTypeEnum? {
            return entries.find { it.value == value }
        }
    }

    fun hasPrivate() = this == PRIVATE
    fun hasGroup() = this == GROUP
    fun hasSuperGroup() = this == SUPERGROUP
    fun hasChannel() = this == CHANNEL
}