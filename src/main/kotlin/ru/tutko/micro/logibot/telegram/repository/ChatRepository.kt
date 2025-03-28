package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.tutko.micro.logibot.telegram.model.entity.Chat
import ru.tutko.micro.logibot.telegram.model.entity.User
import java.util.Optional

interface ChatRepository : JpaRepository<Chat, Long> {

    fun findByOrganization_Id(id: Long): MutableList<Chat>
    fun findByChatId(chatId: Long): Optional<Chat>
	fun existsByChatId(chatId: Long): Boolean
}