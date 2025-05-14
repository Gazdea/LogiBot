package ru.tutko.micro.logibot.telegram.service

import org.springframework.data.domain.Page
import ru.tutko.micro.logibot.telegram.model.dto.OrganizationDto
import ru.tutko.micro.logibot.telegram.model.dto.RoleDto

interface OrganizationService {

    /**
     * Создаёт новую организацию и назначает пользователя её владельцем/создателем.
     *
     * @param name Название организации.
     * @param userId ID пользователя, создающего организацию.
     * @return DTO созданной организации.
     */
    fun createOrganization(name: String, userId: Long): OrganizationDto

    /**
     * Обновляет информацию об организации.
     *
     * @param organizationDto DTO организации с новыми данными.
     * @return Обновлённый DTO организации, либо null, если организация не найдена.
     */
    fun updateOrganization(organizationDto: OrganizationDto): OrganizationDto?

    /**
     * Удаляет организацию по её ID.
     *
     * @param id ID организации.
     */
    fun deleteOrganization(id: Long)

    /**
     * Получает организацию по её ID.
     *
     * @param id ID организации.
     * @return DTO организации.
     */
    fun getOrganizationById(id: Long): OrganizationDto

    /**
     * Возвращает список организаций, к которым имеет доступ пользователь.
     * Используется пагинация.
     *
     * @param userId ID пользователя.
     * @param page Номер страницы.
     * @param size Количество элементов на странице (по умолчанию 8).
     * @return Страница DTO организаций.
     */
    fun getOrganizationsByUserId(userId: Long, page: Int, size: Int = 8): Page<OrganizationDto>

    /**
     * Возвращает список ролей, назначенных пользователям в рамках конкретной организации.
     * Используется пагинация.
     *
     * @param organizationId ID организации.
     * @param page Номер страницы.
     * @param size Количество элементов на странице (по умолчанию 8).
     * @return Страница DTO ролей.
     */
    fun getRolesOrganization(organizationId: Long, page: Int, size: Int = 8): Page<RoleDto>
}
