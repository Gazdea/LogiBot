package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
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

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    var chats: MutableList<Chat> = mutableListOf()

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER)
    var userOrganizationLinks: MutableList<UserOrganizationLink> = mutableListOf()

    override fun toString(): String {
        return "Organization(" +
                "id=$id," +
                "name=$name," +
//                "chats=$chats," +
//                "userOrganizationLinks=$userOrganizationLinks" +
                ")"
    }
}