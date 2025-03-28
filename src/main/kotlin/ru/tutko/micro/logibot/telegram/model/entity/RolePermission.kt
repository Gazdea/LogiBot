package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.Size

@Entity
@Table(name = "role_permission")
open class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_permission_id_gen")
    @SequenceGenerator(name = "role_permission_id_gen", sequenceName = "role_permission_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    var role: Role? = null

    @Size(max = 100)
    @Column(name = "permission", length = 100)
    var permission: String? = null
}