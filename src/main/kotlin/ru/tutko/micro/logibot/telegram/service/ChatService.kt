package ru.tutko.micro.logibot.telegram.service

import ru.tutko.micro.logibot.telegram.model.dto.ChatDto
import ru.tutko.micro.logibot.telegram.model.dto.request.FindOrMigrateChatDtoRequest

import java.util.*

interface ChatService {

    /**
     * Возвращает список всех чатов в системе.
     *
     * @return Список DTO чатов.
     */
    fun getChats(): MutableList<ChatDto>

    /**
     * Возвращает чат по его ID.
     *
     * @param id ID чата.
     * @return Optional с DTO чата, если найден.
     */
    fun getChatById(id: Long): Optional<ChatDto>

    /**
     * Возвращает список чатов, привязанных к конкретной организации.
     *
     * @param organizationId ID организации.
     * @return Список DTO чатов.
     */
    fun getChatsByOrganizationId(organizationId: Long): List<ChatDto>

    /**
     * Создаёт новый чат.
     *
     * @param chat DTO чата с начальными данными.
     * @return Созданный DTO чата.
     */
    fun createChat(chat: ChatDto): ChatDto

    /**
     * Обновляет существующий чат.
     *
     * @param chat DTO чата с обновлёнными данными.
     * @return Обновлённый DTO чата.
     */
    fun updateChat(chat: ChatDto): ChatDto

    /**
     * Удаляет чат по его ID.
     *
     * @param chatId ID удаляемого чата.
     */
    fun deleteChat(chatId: Long)

    /**
     * Ищет существующий чат или выполняет миграцию (если чат сменил ID, например после переустановки/переноса).
     *
     * @param chatMigrateDto DTO с информацией о миграции или поиске.
     * @return Optional с найденным или мигрированным DTO чата.
     */
    fun findOrMigrateChat(chatMigrateDto: FindOrMigrateChatDtoRequest): Optional<ChatDto>

    /**
     * Проверяет, существует ли чат с заданным ID.
     *
     * @param chatId ID чата.
     * @return true, если чат существует.
     */
    fun exists(chatId: Long): Boolean
}
