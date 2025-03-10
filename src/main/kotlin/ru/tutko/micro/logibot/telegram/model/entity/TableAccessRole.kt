package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "table_access_roles")
open class TableAccessRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_access_roles_id_gen")
    @SequenceGenerator(
        name = "table_access_roles_id_gen",
        sequenceName = "table_access_roles_id_seq",
        allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    open var table: ru.tutko.micro.logibot.telegram.model.entity.Table? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    open var role: Role? = null

    @Size(max = 100)
    @Column(name = "permission", length = 100)
    open var permission: String? = null
}