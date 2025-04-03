package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

@Entity
@Table(name = "role_organization_permission")
class RoleOrganizationPermission {
	@EmbeddedId
	var id: RoleOrganizationPermissionId? = null

	@MapsId("organizationId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	var organization: Organization? = null

	@MapsId("roleId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "role_id", nullable = false)
	var role: Role? = null

	@Size(max = 100)
	@NotNull
	@Column(name = "permission", nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	var permission: PermissionAccessEnum? = null
}