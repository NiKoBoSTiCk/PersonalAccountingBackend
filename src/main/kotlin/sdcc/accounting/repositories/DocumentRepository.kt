package sdcc.accounting.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sdcc.accounting.model.Document
import sdcc.accounting.model.ETag
import sdcc.accounting.model.User

@Repository
interface DocumentRepository : JpaRepository<Document, Int> {

    fun existsByName(name: String): Boolean

    fun removeById(id: Int): Document?

    fun findAllByUser(user: User): Set<Document>?

    fun findAllByUserAndTag(user: User, tag: ETag): Set<Document>?

    fun findAllByUserAndYear(user: User, year: Int): Set<Document>?

    fun findAllByUserAndYearAndTag(user: User, year: Int, tag: ETag): Set<Document>?
}