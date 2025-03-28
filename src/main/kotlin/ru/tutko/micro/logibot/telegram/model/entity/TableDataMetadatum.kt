package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "table_data_metadata")
class TableDataMetadatum {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_data_metadata_id_gen")
    @SequenceGenerator(
        name = "table_data_metadata_id_gen",
        sequenceName = "table_data_metadata_id_seq",
        allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    var table: ru.tutko.micro.logibot.telegram.model.entity.Table? = null

    @NotNull
    @Column(name = "mongo_document_id", nullable = false, length = Integer.MAX_VALUE)
    var mongoDocumentId: String? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization? = null

    @Size(max = 50)
    @Column(name = "action", length = 50)
    var action: String? = null

    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null

    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now()
        }
    }
}