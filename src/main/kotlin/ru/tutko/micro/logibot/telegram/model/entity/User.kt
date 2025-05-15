package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "\"user\"")
class User {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
	@SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	var id: Long? = null

	@Column(name = "external_user_id")
	var externalUserId: Long? = null

	@Size(max = 100)
	@Column(name = "username", length = 100)
	var username: String? = null

	@Size(max = 100)
	@Column(name = "first_name", length = 100)
	var firstName: String? = null

	@Size(max = 100)
	@Column(name = "last_name", length = 100)
	var lastName: String? = null

	@CreationTimestamp
	@Column(name = "created_at")
	var createdAt: Instant? = null
}