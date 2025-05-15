package ru.tutko.micro.logibot.telegram.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.tutko.micro.logibot.telegram.telegram_update.TelegramBot

@Configuration
class TelegramBotConfig(
    private val telegramBot: TelegramBot
) {
    @Bean
    fun telegramBotsApi(): TelegramBotsApi {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(telegramBot)
        return botsApi
    }
}