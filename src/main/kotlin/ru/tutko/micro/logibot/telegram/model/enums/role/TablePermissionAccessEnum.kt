package ru.tutko.micro.logibot.telegram.model.enums.role

enum class TablePermissionAccessEnum(val nameRu: String, val isVisible: Boolean = true) {
    CREATOR("Полные права", false), // имеет полные права
    VIEW_TABLE("Право видеть таблицу"),  // имеет право просматривать таблицу
    CREATE_REPORT("Право создавать отчёты по таблице"),  // имеет право создавать отчёты по таблице
    VIEW_METADATA("Право просматривать записи таблицы"),  // имеет право просматривать метаданные таблицы
    FILL_METADATA("Право заполнять таблицу"),  // имеет право заполнять таблицу
    EDIT_METADATA("Право изменять записи таблицы"), // имеет право изменять метаданные таблицы
    EDIT_TABLE("Право изменять таблицу"),  // имеет право изменять таблицу
    MANAGE_TABLE_PERMISSIONS("Право управлять правами доступа к таблице"),  // имеет право управлять правами доступа к таблице
    DELETE_TABLE("Право удалять таблицу или её данные");  // имеет право удалять таблицу или её данные

    companion object {
        // Сопоставим, какие права автоматически подразумевают VIEW_TABLE
        private val impliesViewTable = setOf(
            CREATE_REPORT,
            EDIT_TABLE,
            DELETE_TABLE,
            MANAGE_TABLE_PERMISSIONS,
            VIEW_METADATA,
            FILL_METADATA,
            EDIT_METADATA
        )

        fun normalizePermissions(permissions: Set<TablePermissionAccessEnum>): Set<TablePermissionAccessEnum> {
            val result = permissions.toMutableSet()
            if (permissions.any { it in impliesViewTable }) {
                result.add(VIEW_TABLE)
            }
            if (permissions.contains(CREATOR)) {
                // CREATOR включает все
                result.addAll(entries.toTypedArray())
            }
            return result
        }
    }
}
