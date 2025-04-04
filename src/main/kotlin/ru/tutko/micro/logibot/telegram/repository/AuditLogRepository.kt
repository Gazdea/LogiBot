package ru.tutko.micro.logibot.telegram.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import ru.tutko.micro.logibot.telegram.model.entity.AuditLog

interface AuditLogRepository : JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {
}