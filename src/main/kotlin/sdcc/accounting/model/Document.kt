package sdcc.accounting.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import sdcc.accounting.dto.DocumentDto
import java.sql.Blob

@Entity
@Table(name = "document", schema = "sdcc")
open class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "id", nullable = false)
    open var id: Int? = null
        protected set

    @ManyToOne(cascade = [CascadeType.PERSIST], optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    open var user: User? = null


    @JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "amount", nullable = false)
    open var amount: Int? = null


    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "description")
    open var description: String? = null

    @JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "year", nullable = false)
    open var year: Int? = null


    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "filename", nullable = false)
    open var filename: String? = null

    @Lob
    @JsonIgnore
    @JdbcTypeCode(SqlTypes.BLOB)
    @Column(name = "file", nullable = false)
    open var file: Blob? = null

    @JdbcTypeCode(SqlTypes.VARCHAR)
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
        tag!!.name
    )
}