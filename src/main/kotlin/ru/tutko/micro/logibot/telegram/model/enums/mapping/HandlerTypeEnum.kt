package ru.tutko.micro.logibot.telegram.model.enums.mapping

enum class HandlerTypeEnum {
    COMMAND,
    CALLBACK,
    INPUT,
    CHAT_MEMBER,
    MY_CHAT_MEMBER,
    UNKNOWN;

    companion object {
        private val mapping = mapOf(
            COMMAND to CommandEnum.entries,
            CALLBACK to CallbackQueryEnum.entries,
            INPUT to InputEnum.entries,
            CHAT_MEMBER to ChatMemberEnum.entries,
            MY_CHAT_MEMBER to ChatMemberEnum.entries,
            UNKNOWN to null
        )

        fun getEnums(type: HandlerTypeEnum): List<Enum<*>>? = mapping[type]
    }
}