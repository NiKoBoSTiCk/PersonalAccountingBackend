package sdcc.accounting.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "user", schema = "sdcc")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "email", nullable = false, unique = true, length = 320)
    open var email: String? = null

    @Column(name = "username", nullable = false, unique = true, length = 50)
    open var username: String? = null

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 64)
    open var password: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}