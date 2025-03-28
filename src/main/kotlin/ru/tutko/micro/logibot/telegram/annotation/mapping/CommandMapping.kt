package ru.tutko.micro.logibot.telegram.annotation.mapping

import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandMapping(val command: CommandEnum)
