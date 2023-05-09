package sdcc.accounting.repositories;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import sdcc.accounting.entities.Document
import sdcc.accounting.entities.Tag
import sdcc.accounting.entities.User
import java.time.Year

@Repository
interface DocumentRepository : JpaRepository<Document, Int> {
    fun removeDocumentById(id: Int): Document?
    fun findDocumentsByUserEqualsAndYearEqualsAndTagEquals(user: User, year: Year, tag: Tag): List<Document>?
}