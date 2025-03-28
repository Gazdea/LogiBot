package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "audit_logs")
class AuditLogs {
    @Id
    @ColumnDefault("nextval('audit_logs_id_seq')")
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    var table: ru.tutko.micro.logibot.telegram.model.entity.Table? = null

    @Size(max = 50)
    @Column(name = "action", length = 50)
    var action: String? = null

    @Column(name = "\"timestamp\"", nullable = false)
    var timestamp: Instant? = null

    @PrePersist
    fun prePersist() {
        if (timestamp == null) {
            timestamp = Instant.now()
        }
    }
}