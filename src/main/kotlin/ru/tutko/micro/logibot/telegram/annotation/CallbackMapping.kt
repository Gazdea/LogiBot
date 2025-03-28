package ru.tutko.micro.logibot.telegram.annotation

import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CallbackMapping(val callbackQuery: CallbackQueryEnum)
