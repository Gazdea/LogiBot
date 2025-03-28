package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.validation.constraints.NotNull
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
class UserOrganizationLinkId : Serializable {
    @NotNull
    @Column(name = "user_id", nullable = false)
    var userId: Long? = null

    @NotNull
    @Column(name = "organization_id", nullable = false)
    var organizationId: Long? = null
    override fun hashCode(): Int = Objects.hash(userId, organizationId)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false

        other as UserOrganizationLinkId

        return userId == other.userId &&
                organizationId == other.organizationId
    }

    companion object {
        private const val serialVersionUID = 2493713702054652382L
    }
}