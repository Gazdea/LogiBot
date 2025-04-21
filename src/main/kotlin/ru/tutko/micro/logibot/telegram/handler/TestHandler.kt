package ru.tutko.micro.logibot.telegram.handler

import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendLocation
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import ru.tutko.micro.logibot.telegram.annotation.*
import ru.tutko.micro.logibot.telegram.annotation.mapping.CallbackMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.CommandMapping
import ru.tutko.micro.logibot.telegram.annotation.mapping.InputMapping
import ru.tutko.micro.logibot.telegram.component.TelegramKeyboard
import ru.tutko.micro.logibot.telegram.exception.NotFoundException
import ru.tutko.micro.logibot.telegram.exception.ValidationException
import ru.tutko.micro.logibot.telegram.model.*
import ru.tutko.micro.logibot.telegram.model.data.TestData
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CallbackQueryEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.CommandEnum
import ru.tutko.micro.logibot.telegram.model.enums.mapping.InputEnum
import ru.tutko.micro.logibot.telegram.util.UpdateUtil
import kotlin.random.Random

@Handlers
class TestHandler(
    private val telegramKeyboard: TelegramKeyboard
)
{

    @CommandMapping(CommandEnum.TEST)
    fun test(request: Request): Response {
        return Response(
            botApiMethods = listOf(SendMessage().apply {
                chatId = request.update.message.chatId.toString()
                text = "Проверка работоспособности"
                replyMarkup = telegramKeyboard.createInlineKeyboard("${request.userId}",
                    listOf(
                        "Проверка input" to CallbackData(CallbackQueryEnum.TEST_INPUT),
                        "Проверка карт" to CallbackData(CallbackQueryEnum.TEST_MAP),
                        "Закрыть" to CallbackData(CallbackQueryEnum.TEST_CLOSE)
                    )
                )
            }
            )
        )
    }

    @CallbackMapping(CallbackQueryEnum.TEST_INPUT)
    fun testCallback(request: Request): Response {
        val rndInt = Random.nextInt(1, 100)
        val rndLong = Random.nextLong(1, 100)
        val testData = TestData(rndInt, rndLong)
        return Response(
            inputType = CallbackData(InputEnum.TEST, testData),
            botApiMethods = listOf(
                SendMessage().apply {
                chatId = request.chatId.toString()
                text = "Ожидается сообщение int $rndInt long $rndLong"
            }
            )
        )
    }

    @InputMapping(InputEnum.TEST)
    fun testInput(request: Request): Response {
        val testData = request.data?.data as TestData
        return Response(
            clearWaitingForInput = true,
            botApiMethods = listOf(SendMessage().apply {
                chatId = request.chatId.toString()
                text = "я прочитал ${request.update.message.text} int: ${testData.rndInt} long: ${testData.rndLong}"
            }),
        )
    }

    @CallbackMapping(CallbackQueryEnum.TEST_MAP)
    fun testMap(request: Request): Response {
        return Response(
            botApiMethods = listOf(SendLocation().apply {
                chatId = request.chatId.toString()
                latitude = 1242.1
                longitude = 221.4
            })
        )
    }

    @CallbackMapping(CallbackQueryEnum.TEST_CLOSE)
    fun testClose(request: Request): Response {
        return Response(
            botApiMethods = listOf(EditMessageText().apply {
                chatId = request.chatId.toString()
                messageId = request.update.callbackQuery.message.messageId
                text = "Закрыто"
            })
        )
    }

    @CommandMapping(CommandEnum.TEST_EXCEPTION)
    fun testException(request: Request): Response {
        throw NotFoundException()
    }
}