package sdcc.accounting.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "user", schema = "sdcc")
open class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    open var id: Int? = null

    @Column(name = "username", unique = true, nullable = false, length = 50)
    open var username: String? = null

    @Column(name = "email", unique = true, nullable = false, length = 320)
    open var email: String? = null

    @Column(name = "password", nullable = false)
    @JsonIgnore
    open var password: String? = null
}