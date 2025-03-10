package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "table_column")
open class TableColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_column_id_gen")
    @SequenceGenerator(name = "table_column_id_gen", sequenceName = "table_column_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    open var table: ru.tutko.micro.logibot.telegram.model.entity.Table? = null

    @Size(max = 255)
    @NotNull
    @Column(name = "column_name", nullable = false)
    open var columnName: String? = null

    @Size(max = 50)
    @NotNull
    @Column(name = "column_type", nullable = false, length = 50)
    open var columnType: String? = null
}