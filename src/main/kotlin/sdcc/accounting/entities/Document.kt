package sdcc.accounting.entities

import jakarta.persistence.*
import lombok.Data
import org.hibernate.Hibernate
import java.sql.Blob
import java.time.Year

@Entity
@Table(name = "Document", schema = "sdcc")
@Data
open class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idDocument", nullable = false)
    open var idDocument: Int? = null

    @Column(name = "amount", nullable = false)
    open var amount: Int? = null

    @Column(name = "description", nullable = true)
    open var description: String? = null

    @Column(name = "year", nullable = false)
    open var year: Year? = null

    @Lob
    @Column(name = "file", nullable = false)
    open var file: Blob? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "User_idUser", nullable = false)
    open var user: User? = null

    @ManyToOne(optional = false)
    @JoinColumn(name = "Tag_idTag", nullable = false)
    open var tag: Tag? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Document

        return idDocument != null && idDocument == other.idDocument
    }

    override fun hashCode(): Int = javaClass.hashCode()
}