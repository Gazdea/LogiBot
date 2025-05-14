package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum

@Entity
@Table(name = "role_organization_permission")
class RoleOrganizationPermission {

	@EmbeddedId
	lateinit var id: RoleOrganizationPermissionId

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("organizationId")
	@JoinColumn(name = "organization_id", nullable = false)
	lateinit var organization: Organization

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@MapsId("roleId")
	@JoinColumn(name = "role_id", nullable = false)
	lateinit var role: Role

	@Enumerated(EnumType.STRING)
	@Column(name = "permission", nullable = false, length = 100, insertable = false, updatable = false)
	lateinit var permission: PermissionAccessEnum
}
