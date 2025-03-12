package ru.tutko.micro.logibot.telegram.annotation

import ru.tutko.micro.logibot.telegram.model.enums.BotInput
import ru.tutko.micro.logibot.telegram.model.enums.PermissionAccess

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InputHandler(val input: BotInput, val access: PermissionAccess = PermissionAccess.DEFAULT)