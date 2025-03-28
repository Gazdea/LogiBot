package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "role")
class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_gen")
    @SequenceGenerator(name = "role_id_gen", sequenceName = "role_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Size(max = 50)
    @Column(name = "role", length = 50)
    var role: String? = null

    @NotNull
    @Column(name = "organization_id", nullable = false)
    var organizationId: Long? = null

    @OneToMany(mappedBy = "role")
    var rolePermissions: MutableSet<RolePermission> = mutableSetOf()

    override fun toString(): String {
        return "Role(" +
                "id=$id, " +
                "role=$role, " +
                "organizationId=$organizationId, " +
//                "rolePermissions=$rolePermissions" +
                ")"
    }
}