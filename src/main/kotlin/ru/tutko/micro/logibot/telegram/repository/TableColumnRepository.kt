package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.TableColumn

interface TableColumnRepository : JpaRepository<TableColumn, Long>, JpaSpecificationExecutor<TableColumn> {
}