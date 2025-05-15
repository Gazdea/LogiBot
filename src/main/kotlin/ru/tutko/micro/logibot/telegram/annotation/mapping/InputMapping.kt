package ru.tutko.micro.logibot.telegram.annotation.mapping

import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InputMapping(val input: InputEnum)