package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import ru.tutko.micro.logibot.telegram.model.enums.ChatTypeEnum
import java.time.Instant

@Entity
@Table(name = "chat")
class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_id_gen")
	@SequenceGenerator(name = "chat_id_gen", sequenceName = "chat_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	var id: Long? = null

	@Column(name = "external_chat_id")
	var externalChatId: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	var organization: Organization? = null

	@Size(max = 50)
	@Column(name = "type", length = 50)
	@Enumerated(EnumType.STRING)
	var type: ChatTypeEnum? = null

	@Size(max = 255)
	@Column(name = "title")
	var title: String? = null

	@Size(max = 100)
	@Column(name = "chat_username", length = 100)
	var chatUsername: String? = null

	@CreationTimestamp
	@Column(name = "created_at")
	var createdAt: Instant? = null
}