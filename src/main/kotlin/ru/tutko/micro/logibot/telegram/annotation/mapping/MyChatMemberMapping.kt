package ru.tutko.micro.logibot.telegram.annotation.mapping

import ru.tutko.micro.logibot.telegram.model.enums.mapping.MyChatMemberEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MyChatMemberMapping(val myChatMember: MyChatMemberEnum)
