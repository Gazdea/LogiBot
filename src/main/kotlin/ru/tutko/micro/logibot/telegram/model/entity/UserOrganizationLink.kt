package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "user_organization_links")
class UserOrganizationLink {
    @EmbeddedId
    @SequenceGenerator(
        name = "user_organization_links_id_gen",
        sequenceName = "table_column_id_seq",
        allocationSize = 1
    )
    var id: UserOrganizationLinkId? = null

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @MapsId("organizationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization? = null

    @Column(name = "role_id")
    var roleId: Long? = null

    @Column(name = "joined_at", updatable = false)
    var joinedAt: Instant? = null

    @PrePersist
    fun prePersist() {
        if (joinedAt == null) {
            joinedAt = Instant.now()
        }
    }

    override fun toString(): String {
        return "UserOrganizationLink(" +
                "id=$id, " +
                "user=$user, " +
                "organization=$organization, " +
                "roleId=$roleId, " +
                "joinedAt=$joinedAt" +
                ")"
    }

}