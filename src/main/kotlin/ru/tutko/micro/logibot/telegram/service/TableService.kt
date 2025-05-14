package ru.tutko.micro.logibot.telegram.service

import org.springframework.data.domain.Page
import ru.tutko.micro.logibot.telegram.model.dto.DataTableDto
import ru.tutko.micro.logibot.telegram.model.dto.TableColumnDto
import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum
import ru.tutko.micro.logibot.telegram.model.table.FilledTableRow
import java.time.Instant
import java.util.UUID

interface TableService {

	/**
	 * Возвращает список всех таблиц, принадлежащих указанной организации.
	 *
	 * @param organizationId ID организации.
	 * @param page страница.
	 * @param size кол-во элементов на странице дефолт 8.
	 * @return Список DTO таблиц.
	 */
	fun getTablesByOrganization(organizationId: Long, page: Int, size: Int = 8): Page<DataTableDto>

	/**
	 * Возвращает конкретную таблицу по её ID.
	 *
	 * @param id ID таблицы.
	 * @return DTO таблицы.
	 */
	fun getTable(tableId: Long): DataTableDto

	/**
	 * Возвращает список таблиц, доступных указанному пользователю в рамках организации.
	 *
	 * @param organizationId ID организации.
	 * @param userId ID пользователя.
	 * @param page страница.
	 * @param size кол-во элементов на странице дефолт 8.
	 * @return Список DTO таблиц.
	 */
	fun getTablesByOrganizationIdAndUserId(organizationId: Long, userId: Long, page: Int, size: Int = 8): Page<DataTableDto>

	/**
	 * Создаёт новую таблицу в указанной организации.
	 *
	 * @param organizationId ID организации.
	 * @param tableName Название таблицы.
	 * @return DTO созданной таблицы.
	 */
	fun createTable(organizationId: Long, tableName: String): DataTableDto

	/**
	 * Удаляет таблицу по её ID.
	 *
	 * @param tableId ID таблицы.
	 * @return true, если удаление прошло успешно.
	 */
	fun deleteTable(tableId: Long): Boolean

	/**
	 * Возвращает список всех колонок, принадлежащих указанной таблице.
	 *
	 * @param tableId ID таблицы
	 * @param page страница.
	 * @param size кол-во элементов на странице дефолт 8.
	 * @return Список DTO таблиц.
	 */
	fun getTableColumns(tableId: Long, page: Int, size: Int = 8): Page<TableColumnDto>

	/**
	 * Возвращает колонку по-указанному id.
	 *
	 * @param tableColumnId ID колонки.
	 * @return Данные о колонке
	 */
	fun getTableColumn(tableColumnId: Long): TableColumnDto

	/**
	 * Добавляет новую колонку в таблицу.
	 *
	 * @param tableId ID таблицы.
	 * @param columnName Название колонки.
	 * @param columnType Тип данных колонки.
	 * @return Обновлённое DTO таблицы.
	 */
	fun addTableColumn(tableId: Long, columnName: String, columnType: ColumnTypeEnum): DataTableDto

	/**
	 * Удаляет колонку по имени из таблицы.
	 *
	 * @param tableColumnId ID колонки таблицы.
	 */
	fun deleteTableColumn(tableColumnId: Long)

	/**
	 * Обновляет имя и тип указанной колонки.
	 *
	 * @param columnId ID колонки.
	 * @param columnName Новое имя.
	 * @param columnType Новый тип данных.
	 * @return Обновлённое DTO таблицы.
	 */
	fun updateTableColumn(columnId: Long, columnName: String, columnType: ColumnTypeEnum): DataTableDto

	/**
	 * Возвращает все строки с заполненными данными (из MongoDB) для указанной таблицы.
	 *
	 * @param tableId ID таблицы.
	 * @param page страница.
	 * @param size кол-во элементов на странице дефолт 8.
	 * @return Список заполненных строк.
	 */
	fun getMetadataTable(tableId: Long, page: Int, size: Int = 8): Page<FilledTableRow>

	/**
	 * Возвращает одну заполненную строку по UUID.
	 *
	 * @param tableId ID таблицы.
	 * @param dataUUID UUID записи в Mongo.
	 * @return Заполненная строка.
	 */
	fun getMetadataEntry(tableId: Long, dataUUID: UUID): FilledTableRow

	/**
	 * Добавляет новую строку заполненных данных в таблицу от имени пользователя.
	 *
	 * @param tableId ID таблицы.
	 * @param userId ID пользователя.
	 * @param data Заполненные данные.
	 * @return Обновлённое DTO таблицы.
	 */
	fun addMetadataTableColumn(tableId: Long, userId: Long, data: FilledTableRow): DataTableDto

	/**
	 * Удаляет одну строку заполненных данных по UUID и пользователю.
	 *
	 * @param tableId ID таблицы.
	 * @param userId ID пользователя (кто удаляет).
	 * @param dataUUID UUID удаляемой строки.
	 * @return Обновлённое DTO таблицы.
	 */
	fun deleteMetadataTableColumn(tableId: Long, userId: Long, dataUUID: UUID): DataTableDto

	/**
	 * Обновляет заполненные данные по UUID.
	 *
	 * @param tableId ID таблицы.
	 * @param userId ID пользователя (кто обновляет).
	 * @param dataUUID UUID обновляемой строки.
	 * @param data Новые данные.
	 * @return Обновлённое DTO таблицы.
	 */
	fun updateMetadataTableColumn(tableId: Long, userId: Long, dataUUID: UUID, data: FilledTableRow): DataTableDto

	/**
	 * Получает строки, отфильтрованные по дате и/или пользователю.
	 *
	 * @param tableId ID таблицы.
	 * @param userId (необязательный) ID пользователя.
	 * @param from (необязательный) дата "от".
	 * @param to (необязательный) дата "до".
	 * @param page страница.
	 * @param size кол-во элементов на странице дефолт 8.
	 * @return Список заполненных строк, подходящих под фильтр.
	 */
	fun getMetadataByFilters(
		tableId: Long,
		userId: Long? = null,
		from: Instant? = null,
		to: Instant? = null,
		page: Int,
		size: Int = 8
	): Page<FilledTableRow>
}
