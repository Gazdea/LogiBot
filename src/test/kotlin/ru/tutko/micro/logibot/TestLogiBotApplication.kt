package ru.tutko.micro.logibot

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<LogiBotApplication>().with(TestcontainersConfiguration::class).run(*args)
}
