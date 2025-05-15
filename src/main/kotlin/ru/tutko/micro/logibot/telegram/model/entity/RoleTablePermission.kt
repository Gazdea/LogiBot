package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.role.TablePermissionAccessEnum

@Entity
@Table(name = "role_table_permission")
class RoleTablePermission {
	@EmbeddedId
	var id: RoleTablePermissionId? = null

	@MapsId("roleId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "role_id", nullable = false)
	var role: Role? = null

	@MapsId("tableId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "table_id", nullable = false)
	var table: DataTable? = null

	@Size(max = 100)
	@NotNull
	@Column(name = "permission", nullable = false, length = 100, insertable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	var permission: TablePermissionAccessEnum? = null
}