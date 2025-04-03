package ru.tutko.micro.logibot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class LogiBotApplication

    fun main(args: Array<String>) {
        runApplication<LogiBotApplication>(*args)
    }

