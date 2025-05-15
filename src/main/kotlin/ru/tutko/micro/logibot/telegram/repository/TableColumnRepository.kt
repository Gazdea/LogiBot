package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.TableColumn
import java.util.Optional

interface TableColumnRepository : JpaRepository<TableColumn, Long>, JpaSpecificationExecutor<TableColumn> {

	override fun deleteById(id: Long)


	fun findByTable_Id(id: Long, pageable: Pageable): Page<TableColumn>

	fun findAllByTableId(tableId: Long): List<TableColumn>
}