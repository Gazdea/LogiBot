package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.InputType
import ru.tutko.micro.logibot.telegram.model.Response

interface InputHandlerInterface {
    fun handleInput(update: Update): Response
    fun getInputType(): InputType
}