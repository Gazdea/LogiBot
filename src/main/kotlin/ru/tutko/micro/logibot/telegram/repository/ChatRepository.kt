package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.Chat
import java.util.*

interface ChatRepository: JpaRepository<Chat, Long> , JpaSpecificationExecutor<Chat> {

	fun findByOrganization_Id(id: Long): List<Chat>

	fun findByExternalChatId(id: Long): Optional<Chat>


	fun existsByExternalChatId(externalChatId: Long): Boolean
}