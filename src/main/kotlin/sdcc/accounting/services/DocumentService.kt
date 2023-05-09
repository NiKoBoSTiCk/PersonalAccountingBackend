package sdcc.accounting.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sdcc.accounting.entities.Document
import sdcc.accounting.payload.request.DocumentRequest
import sdcc.accounting.payload.request.UserRequest
import sdcc.accounting.repositories.DocumentRepository
import sdcc.accounting.repositories.TagRepository
import sdcc.accounting.repositories.UserRepository
import sdcc.accounting.support.exceptions.DocumentNotFoundException
import sdcc.accounting.support.exceptions.TagNotFoundException
import sdcc.accounting.support.exceptions.UserNotFoundException
import java.time.Year

@Service
class DocumentService {

    @Autowired
    var documentRepository: DocumentRepository? = null

    @Autowired
    var tagRepository: TagRepository? = null

    @Autowired
    var userRepository: UserRepository? = null

    @Transactional
    fun addDocument(doc: DocumentRequest) {
        val newDoc = Document()
        newDoc.user = userRepository?.findUserByEmail(doc.email)?: throw UserNotFoundException()
        newDoc.tag = tagRepository?.findTagByTagEquals(enumValueOf(doc.tag))?: throw TagNotFoundException()
        newDoc.amount = doc.amount
        newDoc.description = doc.description
        newDoc.file = doc.file
        newDoc.year = doc.year
        documentRepository!!.saveAndFlush(newDoc)
    }

    @Transactional
    fun removeDocument(id: Int) {
        documentRepository?.removeDocumentById(id)?: throw DocumentNotFoundException()
    }

    @Transactional
    fun updateDocument(doc: DocumentRequest) {
        val newDoc = documentRepository?.removeDocumentById(doc.id)?: throw DocumentNotFoundException()
        newDoc.user = userRepository?.findUserByEmail(doc.email)?: throw UserNotFoundException()
        newDoc.tag = tagRepository?.findTagByTagEquals(enumValueOf(doc.tag))?: throw TagNotFoundException()
        newDoc.amount = doc.amount
        newDoc.description = doc.description
        newDoc.file = doc.file
        newDoc.year = doc.year
        documentRepository!!.saveAndFlush(newDoc)
    }

    @Transactional
    fun showDocumentByUserAndYearAndTag(email: String, year: Year, tag: String): List<Document>? {
        val validUser = userRepository?.findUserByEmail(email)?: throw UserNotFoundException()
        val validTag = tagRepository?.findTagByTagEquals(enumValueOf(tag))?: throw TagNotFoundException()
        return documentRepository!!.findDocumentsByUserEqualsAndYearEqualsAndTagEquals(validUser, year, validTag)
    }
}