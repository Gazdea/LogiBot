package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(name = "organization")
class Organization {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_id_gen")
	@SequenceGenerator(name = "organization_id_gen", sequenceName = "organization_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false)
	var id: Long? = null

	@Size(max = 255)
	@Column(name = "name")
	var name: String? = null

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
	var chats: MutableSet<Chat> = mutableSetOf()

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
	var dataTables: MutableSet<DataTable> = mutableSetOf()

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
	var roles: MutableSet<Role> = mutableSetOf()

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
	var roleOrganizationPermissions: MutableSet<RoleOrganizationPermission> = mutableSetOf()

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
	var userOrganizationLinks: MutableSet<UserOrganizationLink> = mutableSetOf()
}