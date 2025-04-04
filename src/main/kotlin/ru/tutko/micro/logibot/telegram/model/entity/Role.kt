package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "role")
class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_gen")
	@SequenceGenerator(name = "role_id_gen", sequenceName = "role_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	var id: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	var organization: Organization? = null

	@Size(max = 50)
	@NotNull
	@Column(name = "role_name", nullable = false, length = 50)
	var roleName: String? = null

	@OneToMany(mappedBy = "role")
	var roleOrganizationPermissions: MutableSet<RoleOrganizationPermission> = mutableSetOf()

	@OneToMany(mappedBy = "role")
	var roleTablePermissions: MutableSet<RoleTablePermission> = mutableSetOf()
}