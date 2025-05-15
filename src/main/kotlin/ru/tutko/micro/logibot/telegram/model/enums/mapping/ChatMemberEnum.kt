package ru.tutko.micro.logibot.telegram.model.enums.mapping

enum class ChatMemberEnum(val value: String) {
	CREATED("creator"),
	ADMINISTRATOR("administrator"),
	MEMBER("member"),
	RESTRICTED("restricted"),
	LEFT("left"),
	KICKED("kicked"),
}