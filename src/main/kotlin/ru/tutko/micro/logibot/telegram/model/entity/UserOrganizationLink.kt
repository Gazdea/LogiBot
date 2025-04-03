package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "user_organization_links")
class UserOrganizationLink(user: User, organization: Organization, role: Role) {
	@EmbeddedId
	var id: UserOrganizationLinkId? = null

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	var user: User? = user

	@MapsId("organizationId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "organization_id", nullable = false)
	var organization: Organization? = organization

	@MapsId("roleId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "role_id", nullable = false)
	var role: Role? = role

	@CreationTimestamp
	@Column(name = "joined_at")
	var joinedAt: Instant? = null

	init {
		this.id = UserOrganizationLinkId().apply { userId = user.id; organizationId = organization.id; roleId = role.id}
	}
}