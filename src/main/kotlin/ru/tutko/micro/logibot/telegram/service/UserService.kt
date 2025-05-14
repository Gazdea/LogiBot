package ru.tutko.micro.logibot.telegram.service

import org.springframework.data.domain.Page
import ru.tutko.micro.logibot.telegram.model.dto.UserDto
import ru.tutko.micro.logibot.telegram.model.dto.UserOrganizationLinkDto
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import java.util.*

interface UserService {

    /**
     * Возвращает информацию о пользователе по его ID.
     *
     * @param id ID пользователя.
     * @return DTO пользователя.
     */
    fun getUserById(id: Long): UserDto

    /**
     * Возвращает связь пользователя с организацией, используя данные организации и внешнего ID пользователя.
     *
     * @param organizationId ID организации.
     * @param userId ID пользователя.
     * @return DTO связи пользователя с организацией.
     */
    fun getUserOrganizationLinkByUserOrganizationData(organizationId: Long, userId: Long): UserOrganizationLinkDto

    /**
     * Возвращает информацию о пользователе по его внешнему ID.
     *
     * @param userId Внешний ID пользователя (например, Telegram ID).
     * @return DTO пользователя.
     */
    fun getUserByUserExternalId(userId: Long): UserDto

    /**
     * Возвращает информацию о пользователе по имени пользователя (логину).
     *
     * @param username Имя пользователя.
     * @return Optional с DTO пользователя, если найден, иначе пустой Optional.
     */
    fun getUserByUsername(username: String): Optional<UserDto>

    /**
     * Создаёт нового пользователя.
     *
     * @param user DTO нового пользователя.
     * @return DTO созданного пользователя.
     */
    fun createUser(user: UserDto): UserDto

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user DTO пользователя с новыми данными.
     * @return Обновлённый DTO пользователя.
     */
    fun updateUser(user: UserDto): UserDto

    /**
     * Удаляет пользователя по его ID.
     *
     * @param userId ID пользователя.
     */
    fun deleteUser(userId: Long)

    /**
     * Создаёт пользователя, если он ещё не существует. Если пользователь уже существует, возвращает его.
     *
     * @param user DTO пользователя.
     * @return DTO созданного или найденного пользователя.
     */
    fun createIfNotExists(user: UserDto): UserDto

    /**
     * Проверяет существование пользователя по его ID.
     *
     * @param userId ID пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    fun exists(userId: Long): Boolean

    /**
     * Возвращает список пользователей, принадлежащих организации. Используется пагинация.
     *
     * @param organizationId ID организации.
     * @param page Номер страницы.
     * @param size Количество пользователей на странице (по умолчанию 8).
     * @return Страница DTO пользователей.
     */
    fun getUsersByOrganization(organizationId: Long, page: Int, size: Int = 8): Page<UserDto>

    /**
     * Возвращает пользователей, имеющих определённые разрешения в организации. Используется пагинация.
     *
     * @param organizationId ID организации.
     * @param permissions Коллекция проверяемых разрешений.
     * @param include Если true, то фильтруются пользователи с указанными разрешениями, если false — без.
     * @param page Номер страницы.
     * @param size Количество пользователей на странице (по умолчанию 8).
     * @return Страница DTO пользователей.
     */
    fun getUsersByOrganizationIdAndPermissions(
        organizationId: Long,
        permissions: Collection<PermissionAccessEnum>,
        include: Boolean = true,
        page: Int,
        size: Int = 8
    ): Page<UserDto>

    /**
     * Обновляет роль пользователя в рамках конкретной организации.
     *
     * @param organizationId ID организации.
     * @param userId ID пользователя.
     * @param roleId ID новой роли для пользователя.
     */
    fun updateUserRoleByOrganization(organizationId: Long, userId: Long, roleId: Long)

    /**
     * Если пользователь ещё не связан с организацией, то выполняет привязку пользователя к организации.
     *
     * @param organizationId ID организации.
     * @param userExternalId Внешний ID пользователя.
     * @return DTO привязанного пользователя.
     */
    fun joinUserOrganizationIfNeeded(organizationId: Long, userExternalId: Long): UserDto
}
