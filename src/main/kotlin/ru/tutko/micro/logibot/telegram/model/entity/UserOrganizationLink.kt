package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "user_organization_links")
open class UserOrganizationLink {
    @EmbeddedId
    @SequenceGenerator(
        name = "user_organization_links_id_gen",
        sequenceName = "table_column_id_seq",
        allocationSize = 1
    )
    open var id: UserOrganizationLinkId? = null

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null

    @MapsId("organizationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    open var organization: Organization? = null

    @Column(name = "role_id")
    open var roleId: Long? = null

    @ColumnDefault("NOW()")
    @Column(name = "joined_at")
    open var joinedAt: Instant? = null
}