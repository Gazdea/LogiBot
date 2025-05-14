package ru.tutko.micro.logibot.telegram.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.exception.NotFoundException
import ru.tutko.micro.logibot.telegram.exception.OrganizationNotFoundException
import ru.tutko.micro.logibot.telegram.exception.UserNotFoundException
import ru.tutko.micro.logibot.telegram.mapper.DataTableMapper
import ru.tutko.micro.logibot.telegram.mapper.OrganizationMapper
import ru.tutko.micro.logibot.telegram.mapper.RoleMapper
import ru.tutko.micro.logibot.telegram.mapper.TableColumnMapper
import ru.tutko.micro.logibot.telegram.mapper.TableDataMetadatumMapper
import ru.tutko.micro.logibot.telegram.mapper.UserMapper
import ru.tutko.micro.logibot.telegram.model.dto.DataTableDto
import ru.tutko.micro.logibot.telegram.model.dto.TableColumnDto
import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum
import ru.tutko.micro.logibot.telegram.model.table.FilledTableRow
import ru.tutko.micro.logibot.telegram.repository.DataTableRepository
import ru.tutko.micro.logibot.telegram.repository.OrganizationRepository
import ru.tutko.micro.logibot.telegram.repository.RoleRepository
import ru.tutko.micro.logibot.telegram.repository.TableColumnRepository
import ru.tutko.micro.logibot.telegram.repository.TableDataMetadataRepository
import ru.tutko.micro.logibot.telegram.repository.UserRepository
import ru.tutko.micro.logibot.telegram.service.TableService
import ru.tutko.micro.logibot.telegram.service.mongo.DynamicCollectionService
import java.time.Instant
import java.util.UUID

@Service
class TableServiceImpl(
	private val dataTableRepository: DataTableRepository,
	private val dataTableMapper: DataTableMapper,

	private val tableColumnRepository: TableColumnRepository,
	private val tableColumnMapper: TableColumnMapper,

	private val tableDataMetadataRepository: TableDataMetadataRepository,
	private val tableDataMetadatumMapper: TableDataMetadatumMapper,

	private val dynamicCollectionService: DynamicCollectionService,

	private val organizationRepository: OrganizationRepository,
	private val organizationMapper: OrganizationMapper,

	private val userRepository: UserRepository,
	private val userMapper: UserMapper,

	private val roleRepository: RoleRepository,
	private val roleMapper: RoleMapper,

	): TableService {

	@Transactional
	override fun getTablesByOrganization(organizationId: Long, page: Int, size: Int): Page<DataTableDto> {
		val pageable = PageRequest.of(page, size)
		val organization = organizationRepository.findById(organizationId).orElseThrow { OrganizationNotFoundException("Организация не найдена") }
		val tables = dataTableRepository.findByOrganization_Id(organization.id!!, pageable)

		return tables.map { dataTableMapper.toDto(it) }
	}

	@Transactional
	override fun getTable(tableId: Long): DataTableDto {
		val table = dataTableRepository.findById(tableId).orElseThrow { NotFoundException("Таблица не найдена") }

		return dataTableMapper.toDto(table)
	}

	override fun getTablesByOrganizationIdAndUserId(
		organizationId: Long,
		userId: Long,
		page: Int,
		size: Int
	): Page<DataTableDto> {
		val organization = organizationRepository.findById(organizationId).orElseThrow { OrganizationNotFoundException("Организация не найдена") }
		val user = userRepository.findByExternalUserId(userId).orElseThrow { UserNotFoundException("Пользователь не найден") }
		TODO("Not yet implemented")
	}

	@Transactional
	override fun createTable(
		organizationId: Long,
		tableName: String
	): DataTableDto {
		val organization = organizationRepository.findById(organizationId).orElseThrow { OrganizationNotFoundException("Организация не найдена") }

		val table = dataTableRepository.save(dataTableMapper.toEntity(DataTableDto().apply {
			this.organization = DataTableDto.OrganizationDto().apply {
				this.id = organization.id
			}
			this.tableName = tableName
		}))

		return dataTableMapper.toDto(table)
	}

	override fun deleteTable(tableId: Long): Boolean {
		TODO("Not yet implemented")
	}

	@Transactional
	override fun getTableColumns(
		tableId: Long,
		page: Int,
		size: Int
	): Page<TableColumnDto> {
		val pageable = PageRequest.of(page, size)
		return tableColumnRepository.findByTable_Id(tableId, pageable).map { tableColumnMapper.toDto(it) }
	}

	override fun getTableColumn(tableColumnId: Long): TableColumnDto {
		return tableColumnMapper.toDto(tableColumnRepository.findById(tableColumnId).orElseThrow { NotFoundException("Колонка таблицы не найдена") })
	}

	@Transactional
	override fun addTableColumn(
		tableId: Long,
		columnName: String,
		columnType: ColumnTypeEnum
	): DataTableDto {
		val dataTable = dataTableRepository.findById(tableId).orElseThrow { NotFoundException("Таблица не найдена") }
		tableColumnRepository.save(tableColumnMapper.toEntity(TableColumnDto().apply {
			this.table = TableColumnDto.DataTableDto().apply {
				this.id = dataTable.id
			}
			this.columnName = columnName
			this.columnType = columnType
		}
		))
		return dataTableMapper.toDto(dataTableRepository.findById(tableId).orElseThrow { NotFoundException("Таблица не найдена") })
	}

	override fun deleteTableColumn(tableColumnId: Long) {
		val tableColumn = tableColumnRepository.findById(tableColumnId).orElseThrow { NotFoundException("Колонка таблицы не найдена") }
		tableColumnRepository.delete(tableColumn)
	}

	override fun updateTableColumn(
		columnId: Long,
		columnName: String,
		columnType: ColumnTypeEnum
	): DataTableDto {
		TODO("Not yet implemented")
	}

	override fun getMetadataTable(tableId: Long, page: Int, size: Int): Page<FilledTableRow> {
		TODO("Not yet implemented")
	}

	override fun getMetadataEntry(
		tableId: Long,
		dataUUID: UUID
	): FilledTableRow {
		TODO("Not yet implemented")
	}

	override fun addMetadataTableColumn(
		tableId: Long,
		userId: Long,
		data: FilledTableRow
	): DataTableDto {
		TODO("Not yet implemented")
	}

	override fun deleteMetadataTableColumn(
		tableId: Long,
		userId: Long,
		dataUUID: UUID
	): DataTableDto {
		TODO("Not yet implemented")
	}

	override fun updateMetadataTableColumn(
		tableId: Long,
		userId: Long,
		dataUUID: UUID,
		data: FilledTableRow
	): DataTableDto {
		TODO("Not yet implemented")
	}

	override fun getMetadataByFilters(
		tableId: Long,
		userId: Long?,
		from: Instant?,
		to: Instant?,
		page: Int,
		size: Int
	): Page<FilledTableRow> {
		TODO("Not yet implemented")
	}
}