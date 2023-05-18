package sdcc.accounting.entities

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.sql.Blob

@Entity
@Table(name = "Document", schema = "sdcc")
open class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    open var id: Int? = null

    @Column(name = "amount", nullable = false)
    open var amount: Int? = null

    @Column(name = "description", nullable = true)
    open var description: String? = null

    @Column(name = "year", nullable = false)
    open var year: Int? = null

    @Lob
    @Column(name = "file", nullable = false)
    open var file: Blob? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    open var user: User? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tag", nullable = false)
    open var tag: Tag? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Document

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}