package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
class RoleTablePermissionId : Serializable {
	@NotNull
	@Column(name = "role_id", nullable = false)
	var roleId: Long? = null

	@NotNull
	@Column(name = "table_id", nullable = false)
	var tableId: Long? = null
	override fun hashCode(): Int = Objects.hash(roleId, tableId)
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

		other as RoleTablePermissionId

		return roleId == other.roleId &&
				tableId == other.tableId
	}

	companion object {
		private const val serialVersionUID = 8963105367600567929L
	}
}