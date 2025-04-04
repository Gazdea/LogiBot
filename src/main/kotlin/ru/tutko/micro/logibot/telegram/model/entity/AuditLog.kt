package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import ru.tutko.micro.logibot.telegram.model.enums.ActionLogsEnum
import java.time.Instant

@Entity
@Table(name = "audit_logs")
class AuditLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_logs_id_gen")
	@SequenceGenerator(name = "audit_logs_id_gen", sequenceName = "audit_logs_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	var id: Long? = null

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	var user: User? = null

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "table_id")
	var table: DataTable? = null

	@Size(max = 50)
	@Column(name = "action", length = 50)
	@Enumerated(EnumType.STRING)
	var action: ActionLogsEnum? = null

	@CreationTimestamp
	@Column(name = "\"timestamp\"")
	var timestamp: Instant? = null
}