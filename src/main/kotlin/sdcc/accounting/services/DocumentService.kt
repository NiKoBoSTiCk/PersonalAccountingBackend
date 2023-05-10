package sdcc.accounting.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sdcc.accounting.entities.Document
import sdcc.accounting.payload.request.DocumentRequest
import sdcc.accounting.repositories.DocumentRepository
import sdcc.accounting.repositories.TagRepository
import sdcc.accounting.repositories.UserRepository
import sdcc.accounting.support.exceptions.DocumentNotFoundException
import sdcc.accounting.support.exceptions.TagNotFoundException
import sdcc.accounting.support.exceptions.UserNotFoundException
import java.time.Year

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    @Throws(UserNotFoundException::class, TagNotFoundException::class)
    fun addDocument(doc: DocumentRequest) {
        val newDoc = Document()
        newDoc.user = userRepository.findUserByEmail(doc.email)?: throw UserNotFoundException()
        newDoc.tag = tagRepository.findTagByTagEquals(enumValueOf(doc.tag))?: throw TagNotFoundException()
        newDoc.amount = doc.amount
        newDoc.description = doc.description
        newDoc.file = doc.file
        newDoc.year = doc.year
        documentRepository.saveAndFlush(newDoc)
    }

    @Transactional
    @Throws(DocumentNotFoundException::class)
    fun removeDocument(id: Int) {
        documentRepository.removeDocumentById(id)?: throw DocumentNotFoundException()
    }

    @Transactional
    @Throws(UserNotFoundException::class, TagNotFoundException::class, DocumentNotFoundException::class)
    fun updateDocument(doc: DocumentRequest) {
        val newDoc = documentRepository.removeDocumentById(doc.id)?: throw DocumentNotFoundException()
        newDoc.user = userRepository.findUserByEmail(doc.email)?: throw UserNotFoundException()
        newDoc.tag = tagRepository.findTagByTagEquals(enumValueOf(doc.tag))?: throw TagNotFoundException()
        newDoc.amount = doc.amount
        newDoc.description = doc.description
        newDoc.file = doc.file
        newDoc.year = doc.year
        documentRepository.saveAndFlush(newDoc)
    }

    @Transactional
    @Throws(UserNotFoundException::class, TagNotFoundException::class)
    fun showDocumentByUserAndYearAndTag(email: String, year: Year, tag: String): List<Document>? {
        val validUser = userRepository.findUserByEmail(email)?: throw UserNotFoundException()
        val validTag = tagRepository.findTagByTagEquals(enumValueOf(tag))?: throw TagNotFoundException()
        return documentRepository.findDocumentsByUserEqualsAndYearEqualsAndTagEquals(validUser, year, validTag)
    }
}