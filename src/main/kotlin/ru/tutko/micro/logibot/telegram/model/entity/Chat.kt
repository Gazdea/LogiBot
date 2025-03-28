package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant

@Entity
@Table(name = "chat")
class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id_gen")
    @SequenceGenerator(name = "chat_id_gen", sequenceName = "chat_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @Column(name = "chat_id")
    var chatId: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization? = null

    @Size(max = 50)
    @Column(name = "type", length = 50)
    var type: String? = null

    @Size(max = 255)
    @Column(name = "title")
    var title: String? = null

    @Size(max = 100)
    @Column(name = "username", length = 100)
    var username: String? = null

    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now()
        }
    }

    override fun toString(): String {
        return "Chat(" +
                "id=$id, " +
                "chatId=$chatId, " +
//                "organization=$organization, " +
                "type=$type, " +
                "title=$title, " +
                "username=$username, " +
                "createdAt=$createdAt" +
                ")"
    }


}