package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotNull
import org.hibernate.Hibernate
import ru.tutko.micro.logibot.telegram.model.enums.role.PermissionAccessEnum
import java.io.Serializable
import java.util.*

@Embeddable
class RoleOrganizationPermissionId : Serializable {
	@NotNull
	@Column(name = "organization_id", nullable = false)
	var organizationId: Long? = null

	@NotNull
	@Column(name = "role_id", nullable = false)
	var roleId: Long? = null

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "permission", nullable = false, length = 100)
	var permission: PermissionAccessEnum? = null

	override fun hashCode(): Int = Objects.hash(organizationId, roleId, permission)

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

		other as RoleOrganizationPermissionId

		return organizationId == other.organizationId &&
				roleId == other.roleId &&
				permission == other.permission
	}
}

