package ru.tutko.micro.logibot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LogiBotApplication

fun main(args: Array<String>) {
    runApplication<LogiBotApplication>(*args)
}
