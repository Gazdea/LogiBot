package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "table")
open class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_id_gen")
    @SequenceGenerator(name = "table_id_gen", sequenceName = "table_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    open var chat: Chat? = null

    @Size(max = 255)
    @NotNull
    @Column(name = "table_name", nullable = false)
    open var tableName: String? = null

    @ColumnDefault("NOW()")
    @Column(name = "created_at")
    open var createdAt: Instant? = null
}