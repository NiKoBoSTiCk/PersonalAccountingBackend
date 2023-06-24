package sdcc.accounting.model

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.sql.Blob

@Entity
@Table(name = "document", schema = "sdcc")
open class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    open var id: Int? = null

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    open var user: User? = null

    @Column(name = "amount", nullable = false)
    open var amount: Int? = null

    @Column(name = "description", nullable = true)
    open var description: String? = null

    @Column(name = "year", nullable = false)
    open var year: Int? = null

    @Column(name = "filename", nullable = false)
    open var filename: String? = null

    @Lob
    @Column(name = "file", nullable = false, columnDefinition = "blob")
    open var file: Blob? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false)
    open var tag: ETag? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Document

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}