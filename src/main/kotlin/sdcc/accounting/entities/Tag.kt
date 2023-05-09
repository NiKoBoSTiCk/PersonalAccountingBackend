package sdcc.accounting.entities

import jakarta.persistence.*
import lombok.Data

@Entity
@Table(name = "Tag", schema = "sdcc")
@Data
open class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    open var id: Int? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "tag", unique = true, nullable = false)
    open var tag: ETag? = null

    @OneToMany(mappedBy = "tag", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var documents: MutableList<Document> = mutableListOf()
}