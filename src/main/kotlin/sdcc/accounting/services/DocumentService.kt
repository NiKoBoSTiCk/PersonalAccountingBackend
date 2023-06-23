package sdcc.accounting.services

import jakarta.persistence.EntityManagerFactory
import org.hibernate.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import sdcc.accounting.dto.DocumentDto
import sdcc.accounting.model.Document
import sdcc.accounting.model.ETag
import sdcc.accounting.model.User
import sdcc.accounting.exceptions.*
import sdcc.accounting.repositories.DocumentRepository
import java.time.LocalDate

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val entityManagerFactory: EntityManagerFactory
) {

    @Transactional
    fun addDocument(user: User, docInfo: DocumentDto, docFile: MultipartFile) {
        if (documentRepository.existsByName(docInfo.name)) throw DocumentAlreadyExistsException()
        var checkTag = false
        for (tag in ETag.values())
            if (ETag.valueOf(docInfo.tag) == tag) checkTag = true
        if (!checkTag) throw TagNotFoundException()
        if (docInfo.amount < 0) throw NegativeAmountException()
        if (docInfo.year < 0) throw YearNotValidException()
        if (docInfo.year > LocalDate.now().year) throw YearNotValidException()

        val newDoc = Document()
        newDoc.name = docInfo.name
        newDoc.user = user
        newDoc.amount = docInfo.amount
        newDoc.year = docInfo.year
        newDoc.description = docInfo.description
        newDoc.tag = ETag.valueOf(docInfo.tag)
        val session = entityManagerFactory.createEntityManager().delegate as Session
        newDoc.file = session.lobHelper.createBlob(docFile.inputStream, docFile.size)
        documentRepository.save(newDoc)
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
    fun showDocumentsByTag(user: User, tag: String){
        //TODO
    }

    @Transactional
    @Throws(YearNotValidException::class)
    fun showDocumentsByYear(user: User, year: Int){
        //TODO
    }

    @Transactional
    @Throws(TagNotFoundException::class,YearNotValidException::class)
    fun showDocumentsByYearAndTag(user: User, year: Int, tag: String){
        //TODO
    }
}