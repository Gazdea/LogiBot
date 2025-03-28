package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import java.time.Instant

@Entity
@Table(name = "user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "user_id")
    var userId: Long? = null

    @Size(max = 100)
    @Column(name = "username", length = 100)
    var username: String? = null

    @Size(max = 100)
    @Column(name = "first_name", length = 100)
    var firstName: String? = null

    @Size(max = 100)
    @Column(name = "last_name", length = 100)
    var lastName: String? = null

    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @OneToMany(mappedBy = "user")
    var auditLogs: MutableList<AuditLogs> = mutableListOf()

    @OneToMany(mappedBy = "user")
    var userOrganizationLinks: MutableList<UserOrganizationLink> = mutableListOf()

    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now()
        }
    }

    override fun toString(): String {
        return "User(" +
                "id=$id, " +
                "userId=$userId, " +
                "username=$username, " +
                "firstName=$firstName, " +
                "lastName=$lastName, " +
                "createdAt=$createdAt, " +
//                "auditLogs=$auditLogs, " +
//                "userOrganizationLinks=$userOrganizationLinks" +
                ")"
    }
}