package sdcc.accounting.entities

import jakarta.persistence.*
import lombok.Data

@Entity
@Table(name = "User", schema = "sdcc")
@Data
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    open var id: Int? = null

    @Column(name = "username", unique = true, nullable = false, length = 50)
    open var username: String? = null

    @Column(name = "email", unique = true, nullable = false, length = 320)
    open var email: String? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var documents: MutableList<Document> = mutableListOf()
}