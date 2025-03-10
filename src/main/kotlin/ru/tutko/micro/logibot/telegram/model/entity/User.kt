package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "user")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "user_id")
    open var userId: Long? = null

    @Size(max = 100)
    @Column(name = "username", length = 100)
    open var username: String? = null

    @Size(max = 100)
    @Column(name = "first_name", length = 100)
    open var firstName: String? = null

    @Size(max = 100)
    @Column(name = "last_name", length = 100)
    open var lastName: String? = null

    @ColumnDefault("NOW()")
    @Column(name = "created_at")
    open var createdAt: Instant? = null
}