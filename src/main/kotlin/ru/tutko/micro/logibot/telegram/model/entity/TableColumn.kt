package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import ru.tutko.micro.logibot.telegram.model.enums.ColumnTypeEnum

@Entity
@Table(name = "table_column")
class TableColumn {
	@Id
	@ColumnDefault("nextval('table_column_id_seq')")
	@Column(name = "id", nullable = false)
	var id: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "table_id", nullable = false)
	var table: DataTable? = null

	@Size(max = 255)
	@NotNull
	@Column(name = "column_name", nullable = false)
	var columnName: String? = null

	@Size(max = 50)
	@NotNull
	@Column(name = "column_type", nullable = false, length = 50)
	var columnType: ColumnTypeEnum? = null
}