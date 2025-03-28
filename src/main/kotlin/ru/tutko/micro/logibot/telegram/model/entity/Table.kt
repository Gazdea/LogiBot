package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant

@Entity
@Table(name = "table")
class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_id_gen")
    @SequenceGenerator(name = "table_id_gen", sequenceName = "table_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    var chat: Chat? = null

    @Size(max = 255)
    @NotNull
    @Column(name = "table_name", nullable = false)
    var tableName: String? = null

    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now()
        }
    }
}