package sdcc.accounting.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import sdcc.accounting.entities.Document
import sdcc.accounting.entities.ETag
import sdcc.accounting.entities.Tag
import sdcc.accounting.entities.User
import sdcc.accounting.payload.request.DocumentRequest
import sdcc.accounting.repositories.DocumentRepository
import sdcc.accounting.repositories.TagRepository
import sdcc.accounting.repositories.UserRepository
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
        newDoc.amount = doc.amount
        newDoc.description = doc.description
        newDoc.file = doc.file
        newDoc.tag = tagRepository!!.findTagByTagEquals(enumValueOf(doc.tag))
        newDoc.user = userRepository!!.findUserByEmailEquals(doc.email)
        documentRepository!!.saveAndFlush(newDoc)
    }

    @Transactional
    fun removeDocument(document: Document) {

    }

    @Transactional
    fun updateDocument(document: Document) {

    }

    @Transactional
    fun showDocumentbyUserAndYearAndTag(user: User, year: Year, tag: Tag) {

    }
}