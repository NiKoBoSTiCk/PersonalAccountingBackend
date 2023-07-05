package sdcc.accounting.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import sdcc.accounting.dto.DocumentDto
import java.sql.Blob
import java.util.*

@Entity
@Table(name = "document", schema = "sdcc")
open class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    open var user: User? = null

    @JdbcTypeCode(SqlTypes.DECIMAL)
    @Column(name = "amount", nullable = false)
    open var amount: Float? = null

    @Column(name = "description", length = 200)
    open var description: String? = null

    @Column(name = "year", nullable = false)
    open var year: Int? = null

    @Column(name = "filename", nullable = false, length = 50)
    open var filename: String? = null

    @Lob
    @JsonIgnore
    @Column(name = "file", nullable = false, columnDefinition = "longblob")
    open var file: Blob? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", nullable = false, length = 45)
    open var tag: ETag? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Document

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    open fun toDto(): DocumentDto = DocumentDto(
        id!!,
        filename!!,
        amount!!,
        description!!,
        year!!,
        tag!!.name.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else it.toString()
        }
    )
}