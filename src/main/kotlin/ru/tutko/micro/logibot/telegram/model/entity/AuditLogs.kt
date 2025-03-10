package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "audit_logs")
open class AuditLogs {
    @Id
    @ColumnDefault("nextval('audit_logs_id_seq')")
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open var user: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    open var table: ru.tutko.micro.logibot.telegram.model.entity.Table? = null

    @Size(max = 50)
    @Column(name = "action", length = 50)
    open var action: String? = null

    @ColumnDefault("NOW()")
    @Column(name = "\"timestamp\"")
    open var timestamp: Instant? = null
}