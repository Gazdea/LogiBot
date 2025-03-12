package ru.tutko.micro.logibot.telegram.annotation

import ru.tutko.micro.logibot.telegram.model.enums.BotCommand
import ru.tutko.micro.logibot.telegram.model.enums.PermissionAccess

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandler(val command: BotCommand, val access: PermissionAccess = PermissionAccess.DEFAULT)
