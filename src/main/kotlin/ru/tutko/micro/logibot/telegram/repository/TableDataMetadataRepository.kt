package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.TableDataMetadatum

interface TableDataMetadataRepository : JpaRepository<TableDataMetadatum, Long>,
	JpaSpecificationExecutor<TableDataMetadatum> {


	fun findByTable_Id(id: Long, pageable: Pageable): Page<TableDataMetadatum>
}