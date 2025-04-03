package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity
@Table(name = "organization_settings")
class OrganizationSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_settings_id_gen")
	@SequenceGenerator(name = "organization_settings_id_gen", sequenceName = "organization_settings_id_seq", allocationSize = 1)
	var id: Long? = null

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", nullable = false)
	var organization: Organization? = null

	@Size(max = 100)
	@Column(name = "setting_key", nullable = false)
	var setting_key: String? = null

	@Column(name = "setting_value")
	var setting_value: String? = null

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	var created_at: Instant? = null

	@UpdateTimestamp
	@Column(name = "updated_at")
	var updated_at: Instant? = null
}