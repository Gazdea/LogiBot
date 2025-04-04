package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "data_table")
class DataTable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_table_id_gen")
	@SequenceGenerator(name = "data_table_id_gen", sequenceName = "data_table_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	var id: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	var organization: Organization? = null

	@Size(max = 255)
	@NotNull
	@Column(name = "table_name", nullable = false)
	var tableName: String? = null

	@CreationTimestamp
	@Column(name = "created_at")
	var createdAt: Instant? = null
}