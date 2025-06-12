package ru.tutko.micro.logibot.telegram.service.impl

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.tutko.micro.logibot.telegram.exception.NotFoundException
import ru.tutko.micro.logibot.telegram.model.dto.mongo.TableMetadata
import ru.tutko.micro.logibot.telegram.model.enums.table.ColumnTypeEnum
import ru.tutko.micro.logibot.telegram.repository.DataTableRepository
import ru.tutko.micro.logibot.telegram.repository.OrganizationRepository
import ru.tutko.micro.logibot.telegram.repository.TableColumnRepository
import ru.tutko.micro.logibot.telegram.repository.TableDataMetadataRepository
import ru.tutko.micro.logibot.telegram.service.GenerateExcelReport
import java.io.File

@Service
class GenerateExcelReportImpl(
	private val organizationRepository: OrganizationRepository,
	private val dataTableRepository: DataTableRepository,
	private val tableColumnRepository: TableColumnRepository,
	private val tableDataMetadataRepository: TableDataMetadataRepository,
	private val mongoTemplate: MongoTemplate,

): GenerateExcelReport {

	@Transactional
	override fun generateReportByTable(tableId: Long): File {
		val (columnHeaders, documents, columnTypes) = fetchHeadersAndDocuments(tableId)
		val workbook = XSSFWorkbook()
		val sheet = workbook.createSheet("Отчет")

		val headerStyle = createHeaderStyle(workbook)
		val typeStyles = createTypeStyles(workbook)

		createHeaderRow(sheet, columnHeaders, headerStyle)
		fillSheetData(sheet, documents, columnHeaders, typeStyles, columnTypes)
		autoSizeColumns(sheet, columnHeaders.size)

		return writeWorkbookToFile(workbook, tableId)
	}


	private fun fetchHeadersAndDocuments(tableId: Long): Triple<List<String>, List<TableMetadata>, Map<String, ColumnTypeEnum>> {
		val table = dataTableRepository.findById(tableId)
			.orElseThrow { NotFoundException("Таблица не найдена") }

		val mongoIds = table.tableDataMetadata.mapNotNull { it.mongoDocumentId }
		val documents = mongoTemplate.find(
			Query(Criteria.where("_id").`in`(mongoIds)),
			TableMetadata::class.java
		).toList()

		val columns = tableColumnRepository.findAllByTableId(tableId).sortedBy { it.id }
		val columnHeaders = columns.mapNotNull { it.columnName }
		val columnTypeMap = columns.mapNotNull { col ->
			col.columnName?.let { name ->
				col.columnType?.let { type ->
					name to type
				}
			}
		}.toMap()


		return Triple(columnHeaders, documents, columnTypeMap)
	}



	private fun createHeaderStyle(workbook: XSSFWorkbook): CellStyle {
		return workbook.createCellStyle().apply {
			setFont(workbook.createFont().apply {
				bold = true
				color = IndexedColors.WHITE.index
			})
			fillForegroundColor = IndexedColors.BLUE_GREY.index
			fillPattern = FillPatternType.SOLID_FOREGROUND
			alignment = HorizontalAlignment.CENTER
		}
	}

	private fun createHeaderRow(sheet: Sheet, headers: List<String>, style: CellStyle) {
		val headerRow = sheet.createRow(0)
		headers.forEachIndexed { i, header ->
			val cell = headerRow.createCell(i)
			cell.setCellValue(header)
			cell.cellStyle = style
		}
	}

	private fun fillSheetData(
		sheet: Sheet,
		data: List<TableMetadata>,
		headers: List<String>,
		typeStyles: Map<ColumnTypeEnum, CellStyle>,
		columnTypes: Map<String, ColumnTypeEnum>
	) {
		data.forEachIndexed { rowIndex, doc ->
			val row = sheet.createRow(rowIndex + 1)
			val values = doc.columns.associateBy { it.name }

			headers.forEachIndexed { colIndex, colName ->
				val cell = row.createCell(colIndex)
				val value = values[colName]?.value

				val type = columnTypes[colName] ?: ColumnTypeEnum.STRING
				val style = typeStyles[type]
				cell.cellStyle = style

				when (type) {
					ColumnTypeEnum.DIGITAL, ColumnTypeEnum.MONEY -> cell.setCellValue(value.toString().toDoubleOrNull() ?: 0.0)
					ColumnTypeEnum.BOOLEAN -> cell.setCellValue(if (value.toString() == "true") "Да" else "Нет")
					ColumnTypeEnum.DATE, ColumnTypeEnum.TIME -> {
						cell.setCellValue(value.toString())
					}
					else -> cell.setCellValue(value?.toString() ?: "")
				}
			}
		}
	}


	private fun autoSizeColumns(sheet: Sheet, columnCount: Int) {
		repeat(columnCount) { sheet.autoSizeColumn(it) }
	}

	private fun writeWorkbookToFile(workbook: XSSFWorkbook, fileSuffix: Any): File {
		val file = File.createTempFile("report_${fileSuffix}_", ".xlsx")
		file.outputStream().use { workbook.write(it) }
		workbook.close()
		return file
	}


	@Transactional
	override fun generateReportByOrganization(organizationId: Long): File {
		val tables = dataTableRepository.findAllByOrganizationId(organizationId)
		if (tables.isEmpty()) throw NotFoundException("У организации нет таблиц")

		val workbook = XSSFWorkbook()
		val typeStyles = createTypeStyles(workbook)

		tables.forEach { table ->
			val tableId = table.id ?: return@forEach
			val (columnHeaders, documents, columnTypes) = fetchHeadersAndDocuments(tableId)

			val sheetName = table.tableName?.take(31) ?: "Лист"
			val sheet = workbook.createSheet(sheetName)

			val headerStyle = createHeaderStyle(workbook)
			createHeaderRow(sheet, columnHeaders, headerStyle)
			fillSheetData(sheet, documents, columnHeaders, typeStyles, columnTypes)
			autoSizeColumns(sheet, columnHeaders.size)
		}

		return writeWorkbookToFile(workbook, organizationId)
	}


	private fun createTypeStyles(workbook: XSSFWorkbook): Map<ColumnTypeEnum, CellStyle> {
		fun baseStyle(): CellStyle = workbook.createCellStyle().apply {
			alignment = HorizontalAlignment.LEFT
		}

		val dateStyle = workbook.createCellStyle().apply {
			dataFormat = workbook.creationHelper.createDataFormat().getFormat("dd.MM.yyyy")
		}

		val timeStyle = workbook.createCellStyle().apply {
			dataFormat = workbook.creationHelper.createDataFormat().getFormat("HH:mm:ss")
		}

		val moneyStyle = workbook.createCellStyle().apply {
			dataFormat = workbook.creationHelper.createDataFormat().getFormat("#,##0.00 ₽")
			alignment = HorizontalAlignment.RIGHT
		}

		val numericStyle = workbook.createCellStyle().apply {
			dataFormat = workbook.creationHelper.createDataFormat().getFormat("0")
			alignment = HorizontalAlignment.RIGHT
		}

		return mapOf(
			ColumnTypeEnum.STRING to baseStyle(),
			ColumnTypeEnum.BOOLEAN to baseStyle(),
			ColumnTypeEnum.DATE to dateStyle,
			ColumnTypeEnum.TIME to timeStyle,
			ColumnTypeEnum.MONEY to moneyStyle,
			ColumnTypeEnum.DIGITAL to numericStyle,
		)
	}


}