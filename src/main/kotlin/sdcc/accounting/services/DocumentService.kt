package sdcc.accounting.services

import jakarta.persistence.EntityManagerFactory
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.hibernate.Session
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import sdcc.accounting.dto.DocumentDto
import sdcc.accounting.dto.DocumentFileDto
import sdcc.accounting.exceptions.*
import sdcc.accounting.model.Document
import sdcc.accounting.model.ETag
import sdcc.accounting.model.User
import sdcc.accounting.repositories.DocumentRepository
import java.io.File
import java.time.LocalDate


@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val entityManagerFactory: EntityManagerFactory
) {

    @Transactional
    fun addDocument(user: User, docInfo: DocumentDto, docFile: MultipartFile) {
        if (!docInfo.filename.matches(Regex("^[A-Za-z]{3,}\\.(?:pdf|doc|docx)\$"))) throw FilenameNotValidException()
        if (docInfo.year < 1950 || docInfo.year > LocalDate.now().year) throw YearNotValidException()
        if (docInfo.amount < 0) throw NegativeAmountException()
        if (docFile.isEmpty) throw EmptyDocumentException()
        var checkTag = false
        for (tag in ETag.values())
            if (ETag.valueOf(docInfo.tag) == tag) checkTag = true
        if (!checkTag) throw TagNotFoundException()
        if (documentRepository.existsByFilenameAndUser(docInfo.filename, user)) throw DocumentAlreadyExistsException()

        val newDoc = Document()
        newDoc.filename = docInfo.filename
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
    fun removeDocument(user: User, id: Int) {
        if (!documentRepository.existsById(id)) throw DocumentNotFoundException()
        val document = documentRepository.findById(id).get()
        if (document.user?.id != user.id) throw UserNotValidException()
        documentRepository.removeById(document.id!!)
    }

    @Transactional
    fun updateDocument(user: User, docInfo: DocumentDto, docFile: MultipartFile) {
        if (!docInfo.filename.matches(Regex("^[A-Za-z]{3,}\\.(?:pdf|doc|docx)\$"))) throw FilenameNotValidException()
        if (docInfo.amount < 0) throw NegativeAmountException()
        if (docInfo.year < 1950 || docInfo.year > LocalDate.now().year) throw YearNotValidException()
        if (docInfo.id == null) throw DocumentNotFoundException()
        var checkTag = false
        for (tag in ETag.values())
            if (ETag.valueOf(docInfo.tag) == tag) checkTag = true
        if (!checkTag) throw TagNotFoundException()
        if (!documentRepository.existsById(docInfo.id)) throw DocumentNotFoundException()

        val document = documentRepository.findById(docInfo.id).get()
        if (document.user?.id != user.id) throw UserNotValidException()
        if (docInfo.filename != document.filename && documentRepository.existsByFilenameAndUser(docInfo.filename, user))
            throw DocumentWithSameNameAlreadyExistsException()
        document.filename = docInfo.filename
        document.amount = docInfo.amount
        document.year = docInfo.year
        document.description = docInfo.description
        document.tag = ETag.valueOf(docInfo.tag)
        if (!docFile.isEmpty) {
            val session = entityManagerFactory.createEntityManager().delegate as Session
            document.file = session.lobHelper.createBlob(docFile.inputStream, docFile.size)
        }
        documentRepository.save(document)
    }

    @Transactional
    fun downloadDocument(user: User, id: Int): DocumentFileDto? {
        if (!documentRepository.existsById(id)) throw DocumentNotFoundException()
        val document = documentRepository.findById(id).get()
        if (document.user?.id != user.id) throw UserNotValidException()

        val documentFile = File("$document.filename")
        IOUtils.copy(document.file?.binaryStream, documentFile.outputStream()) //ok fino a 2GB, poi copyLarge(...)
        return DocumentFileDto(document.filename!!, documentFile)
    }

    @Transactional
    fun report() {
        //TODO
    }

    @Transactional
    fun showAllUserDocuments(user: User): Set<Document>? {
        return documentRepository.findAllByUser(user)
    }

    @Transactional
    fun showUserDocumentsByTag(user: User, tag: String): Set<Document>? {
        var checkTag = false
        for (etag in ETag.values())
            if (ETag.valueOf(tag) == etag) checkTag = true
        if (!checkTag) throw TagNotFoundException()
        return documentRepository.findAllByUserAndTag(user, ETag.valueOf(tag))
    }

    @Transactional
    fun showUserDocumentsByYear(user: User, year: Int): Set<Document>? {
        if (year < 1950 || year > LocalDate.now().year) throw YearNotValidException()
        return documentRepository.findAllByUserAndYear(user, year)
    }

    @Transactional
    fun showUserDocumentsByYearAndTag(user: User, year: Int, tag: String): Set<Document>? {
        if (year < 1950 || year > LocalDate.now().year) throw YearNotValidException()
        var checkTag = false
        for (etag in ETag.values())
            if (ETag.valueOf(tag) == etag) checkTag = true
        if (!checkTag) throw TagNotFoundException()
        return documentRepository.findAllByUserAndYearAndTag(user, year, ETag.valueOf(tag))
    }
}