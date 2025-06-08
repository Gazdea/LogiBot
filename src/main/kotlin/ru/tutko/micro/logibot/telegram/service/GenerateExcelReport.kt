package ru.tutko.micro.logibot.telegram.service

import java.io.File

interface GenerateExcelReport {

	fun generateReportByTable(tableId: Long): File

	fun generateReportByOrganization(organizationId: Long): File
}