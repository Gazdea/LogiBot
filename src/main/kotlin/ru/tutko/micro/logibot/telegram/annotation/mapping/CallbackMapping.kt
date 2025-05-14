package ru.tutko.micro.logibot.telegram.annotation.mapping

import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CallbackMapping(vararg val callbackQuery: CallbackQueryEnum)
