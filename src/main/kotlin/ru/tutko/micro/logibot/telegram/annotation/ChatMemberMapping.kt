package ru.tutko.micro.logibot.telegram.annotation

import ru.tutko.micro.logibot.telegram.model.enums.mapping.ChatMemberEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChatMemberMapping(val chatMember: ChatMemberEnum)
