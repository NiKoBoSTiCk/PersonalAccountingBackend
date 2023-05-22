package sdcc.accounting.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sdcc.accounting.entities.Document
import sdcc.accounting.entities.User
import sdcc.accounting.exceptions.*
import sdcc.accounting.repositories.DocumentRepository
import sdcc.accounting.repositories.TagRepository
import java.time.LocalDate

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val tagRepository: TagRepository
) {

    @Transactional
    @Throws(
        TagNotFoundException::class, InvalidUserException::class, InvalidTagException::class,
        YearNotValidException::class, NegativeAmountException::class
    )
    fun addDocument(user: User, doc: Document) {
        if (doc.user?.id != user.id) throw InvalidUserException()
        if (!tagRepository.existsByTag(doc.tag?.tag!!)) throw InvalidTagException()
        if (doc.amount!! < 0) throw NegativeAmountException()
        if (doc.year!! < 0) throw YearNotValidException()
        if (doc.year!! > LocalDate.now().year) throw YearNotValidException()
        documentRepository.save(doc)
    }

    @Transactional
    @Throws(DocumentNotFoundException::class, InvalidUserException::class)
    fun removeDocument(user: User, id: Int) {
        if (!documentRepository.existsById(id)) throw DocumentNotFoundException()
        val document = documentRepository.findById(id).get()
        if (document.user?.id != user.id) throw InvalidUserException()
        documentRepository.removeById(id)
    }

    @Transactional
    @Throws(
        TagNotFoundException::class, DocumentNotFoundException::class, YearNotValidException::class,
        NegativeAmountException::class, InvalidUserException::class
    )
    fun updateDocument(user: User, doc: Document) {
        if (doc.user?.id != user.id) throw InvalidUserException()
        if (!tagRepository.existsByTag(doc.tag?.tag!!)) throw TagNotFoundException()
        if (!documentRepository.existsById(doc.id!!)) throw DocumentNotFoundException()
        val document = documentRepository.findById(doc.id!!).get()
        if (document.id != doc.id!!) throw DocumentNotFoundException()
        if (doc.amount!! < 0) throw NegativeAmountException()
        if (doc.year!! < 0) throw YearNotValidException()
        if (doc.year!! > LocalDate.now().year) throw YearNotValidException()
        documentRepository.save(doc)
    }

    @Transactional
    fun showAllDocuments(user: User): Set<Document>? {
        return documentRepository.findAllByUser(user)
    }

    @Transactional
    @Throws(TagNotFoundException::class)
    fun showDocumentsByTag(user: User, tag: String): Set<Document>? {
        val validTag = tagRepository.findByTag(enumValueOf(tag))?: throw TagNotFoundException()
        return documentRepository.findAllByUserAndTag(user, validTag)
    }

    @Transactional
    @Throws(YearNotValidException::class)
    fun showDocumentsByYear(user: User, year: Int): Set<Document>? {
        if (year < 0) throw YearNotValidException()
        if (year > LocalDate.now().year) throw YearNotValidException()
        return documentRepository.findAllByUserAndYear(user, year)
    }

    @Transactional
    @Throws(TagNotFoundException::class,YearNotValidException::class)
    fun showDocumentsByYearAndTag(user: User, year: Int, tag: String): Set<Document>? {
        if (year < 0) throw YearNotValidException()
        if (year > LocalDate.now().year) throw YearNotValidException()
        val validTag = tagRepository.findByTag(enumValueOf(tag))?: throw TagNotFoundException()
        return documentRepository.findAllByUserAndYearAndTag(user, year, validTag)
    }
}