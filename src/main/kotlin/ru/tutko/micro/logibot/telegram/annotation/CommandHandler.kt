package ru.tutko.micro.logibot.telegram.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandler(val command: String)
