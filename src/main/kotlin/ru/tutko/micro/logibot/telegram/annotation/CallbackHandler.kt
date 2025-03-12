package ru.tutko.micro.logibot.telegram.annotation

import ru.tutko.micro.logibot.telegram.model.enums.BotCallbackQuery
import ru.tutko.micro.logibot.telegram.model.enums.PermissionAccess


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CallbackHandler(val callbackQuery: BotCallbackQuery, val access: PermissionAccess = PermissionAccess.DEFAULT)
