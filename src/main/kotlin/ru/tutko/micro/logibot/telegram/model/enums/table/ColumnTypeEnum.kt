package ru.tutko.micro.logibot.telegram.model.enums.table

enum class ColumnTypeEnum(val value: String) {

	DIGITAL("Числовой"),
	STRING("Строковый"),
	MONEY("Денежный"),
	DATE("Дата"),
	TIME("Время"),
	BOOLEAN("Да/Нет"),

}