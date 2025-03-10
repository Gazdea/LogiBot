package ru.tutko.micro.logibot.telegram.handler

import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Update
import ru.tutko.micro.logibot.telegram.model.Response

interface ButtonHandlerInterface {
    fun handleButtonPress(update: Update): Boolean
    fun getResponse(update: Update): Response
}