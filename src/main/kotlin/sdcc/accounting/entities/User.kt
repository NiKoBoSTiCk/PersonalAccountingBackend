package sdcc.accounting.entities

import jakarta.persistence.*
import lombok.Data

@Entity
@Table(name = "User", schema = "sdcc")
@Data
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idUser", nullable = false)
    open var idUser: Int? = null

    @Column(name = "name", nullable = false, length = 50)
    open var name: String? = null

    @Column(name = "surname", nullable = false, length = 50)
    open var surname: String? = null

    @Column(name = "email", nullable = false, length = 320)
    open var email: String? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var documents: MutableList<Document> = mutableListOf()
}