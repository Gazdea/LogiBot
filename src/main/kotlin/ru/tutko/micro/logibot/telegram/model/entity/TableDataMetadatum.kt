package ru.tutko.micro.logibot.telegram.model.entity

import jakarta.persistence.*
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "table_data_metadata")
open class TableDataMetadatum {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_data_metadata_id_gen")
    @SequenceGenerator(
        name = "table_data_metadata_id_gen",
        sequenceName = "table_data_metadata_id_seq",
        allocationSize = 1
    )
    @Column(name = "id", nullable = false)
    open var id: Long? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "table_id", nullable = false)
    open var table: ru.tutko.micro.logibot.telegram.model.entity.Table? = null

    @NotNull
    @Column(name = "mongo_document_id", nullable = false, length = Integer.MAX_VALUE)
    open var mongoDocumentId: String? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    open var user: User? = null

    @ColumnDefault("NOW()")
    @Column(name = "created_at")
    open var createdAt: Instant? = null

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    open var organization: Organization? = null

    @Size(max = 50)
    @Column(name = "action", length = 50)
    open var action: String? = null
}