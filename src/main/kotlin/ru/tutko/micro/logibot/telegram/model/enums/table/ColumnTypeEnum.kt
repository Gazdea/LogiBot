package ru.tutko.micro.logibot.telegram.model.enums.table

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class ColumnTypeEnum(val value: String) {

	DIGITAL("Числовой"),
	STRING("Строковый"),
	MONEY("Денежный"),
	DATE("Дата"),
	TIME("Время"),
	BOOLEAN("Да/Нет");

	fun isValid(value: String): Boolean {
		return when (this) {
			DIGITAL -> value.toBigDecimalOrNull() != null
			STRING -> true
			MONEY -> value.toBigDecimalOrNull() != null
			DATE -> listOf(
				"yyyy-MM-dd",
				"dd.MM.yyyy",
				"dd/MM/yyyy"
			).any { pattern ->
				runCatching {
					LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern))
				}.isSuccess
			}

			TIME -> listOf(
				"HH:mm",
				"HH:mm:ss"
			).any { pattern ->
				runCatching {
					LocalTime.parse(value, DateTimeFormatter.ofPattern(pattern))
				}.isSuccess
			}
			BOOLEAN -> value.lowercase() in listOf("да", "нет", "true", "false", "yes", "not")
		}
	}
}