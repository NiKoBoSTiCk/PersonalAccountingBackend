package sdcc.accounting.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sdcc.accounting.entities.Document
import sdcc.accounting.entities.User
import sdcc.accounting.payloads.requests.DocumentRequest
import sdcc.accounting.repositories.DocumentRepository
import sdcc.accounting.repositories.TagRepository
import sdcc.accounting.exceptions.DocumentNotFoundException
import sdcc.accounting.exceptions.TagNotFoundException
import java.time.Year

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val tagRepository: TagRepository
) {

    @Transactional
    @Throws(TagNotFoundException::class)
    fun addDocument(user: User, doc: DocumentRequest) {
        val newDoc = Document()
        newDoc.user = user
        newDoc.tag = tagRepository.findByTag(enumValueOf(doc.getTag()))?: throw TagNotFoundException()
        newDoc.amount = doc.getAmount()
        newDoc.description = doc.getDescription()
        newDoc.file = doc.getFile()
        newDoc.year = doc.getYear()
        documentRepository.save(newDoc)
    }

    @Transactional
    @Throws(DocumentNotFoundException::class)
    fun removeDocument(user: User, doc: DocumentRequest) {
        val document = documentRepository.findById(doc.getId())
        if (document.isEmpty) throw DocumentNotFoundException()
        if (document.get().user?.id != user.id) throw DocumentNotFoundException()
        documentRepository.removeById(doc.getId())?: throw DocumentNotFoundException()
    }

    @Transactional
    @Throws(TagNotFoundException::class, DocumentNotFoundException::class)
    fun updateDocument(user: User, doc: DocumentRequest) {
        val document = documentRepository.findById(doc.getId())
        if (document.isEmpty) throw DocumentNotFoundException()
        if (document.get().user?.id != user.id) throw DocumentNotFoundException()
        val newDoc = documentRepository.removeById(doc.getId())?: throw DocumentNotFoundException()
        newDoc.user = user
        newDoc.tag = tagRepository.findByTag(enumValueOf(doc.getTag()))?: throw TagNotFoundException()
        newDoc.amount = doc.getAmount()
        newDoc.description = doc.getDescription()
        newDoc.file = doc.getFile()
        newDoc.year = doc.getYear()
        documentRepository.save(newDoc)
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
    fun showDocumentsByYear(user: User, year: Year): Set<Document>? {
        return documentRepository.findAllByUserAndYear(user, year)
    }

    @Transactional
    @Throws(TagNotFoundException::class)
    fun showDocumentsByYearAndTag(user: User, year: Year, tag: String): Set<Document>? {
        val validTag = tagRepository.findByTag(enumValueOf(tag))?: throw TagNotFoundException()
        return documentRepository.findAllByUserAndYearAndTag(user, year, validTag)
    }
}