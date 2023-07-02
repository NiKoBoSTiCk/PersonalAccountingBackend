package sdcc.accounting.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sdcc.accounting.model.Document
import sdcc.accounting.model.ETag
import sdcc.accounting.model.User

@Repository
interface DocumentRepository : JpaRepository<Document, Int> {
    fun existsByFilenameAndUser(filename: String, user: User): Boolean

    fun removeById(id: Int)

    fun findByUser(user: User): List<Document>

    fun findByUserAndTag(user: User, tag: ETag): List<Document>

    fun findByUserAndYear(user: User, year: Int): List<Document>

    fun findByUserAndYearAndTag(user: User, year: Int, tag: ETag): List<Document>
}