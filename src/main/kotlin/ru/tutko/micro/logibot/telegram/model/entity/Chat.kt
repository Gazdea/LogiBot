package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "chat")
open class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id_gen")
    @SequenceGenerator(name = "chat_id_gen", sequenceName = "chat_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @Column(name = "chat_id")
    open var chatId: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    open var organization: Organization? = null

    @Size(max = 50)
    @Column(name = "type", length = 50)
    open var type: String? = null

    @Size(max = 255)
    @Column(name = "title")
    open var title: String? = null

    @Size(max = 100)
    @Column(name = "username", length = 100)
    open var username: String? = null

    @ColumnDefault("NOW()")
    @Column(name = "created_at")
    open var createdAt: Instant? = null
}