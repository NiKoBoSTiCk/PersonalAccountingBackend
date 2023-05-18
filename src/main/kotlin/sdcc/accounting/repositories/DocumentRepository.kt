package sdcc.accounting.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sdcc.accounting.entities.Document
import sdcc.accounting.entities.Tag
import sdcc.accounting.entities.User

@Repository
interface DocumentRepository : JpaRepository<Document, Int> {

    fun removeById(id: Int): Document?

    fun findAllByUser(user: User): Set<Document>?

    fun findAllByUserAndTag(user: User, tag: Tag): Set<Document>?

    fun findAllByUserAndYear(user: User, year: Int): Set<Document>?

    fun findAllByUserAndYearAndTag(user: User, year: Int, tag: Tag): Set<Document>?
}