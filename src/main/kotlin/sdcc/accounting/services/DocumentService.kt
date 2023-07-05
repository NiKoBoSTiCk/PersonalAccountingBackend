package sdcc.accounting.services

import jakarta.persistence.EntityManagerFactory
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
import java.time.LocalDate
import java.util.*


@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val entityManagerFactory: EntityManagerFactory
) {

    @Transactional
    fun addDocument(user: User, docInfo: DocumentDto, docFile: MultipartFile) {
        if (!docInfo.filename.matches(Regex("^[_A-Za-z0-9]{3,}\\.(?:pdf|doc|docx)\$"))) throw FilenameNotValidException()
        if (docInfo.year < 1950 || docInfo.year > LocalDate.now().year) throw YearNotValidException()
        if (docInfo.amount < 0) throw AmountIsNegativeException()
        if (docFile.isEmpty) throw DocumentFileIsEmptyException()
        if (!ETag.values().any { it.name == docInfo.tag.uppercase(Locale.getDefault()) }) throw TagNotFoundException()
        if (documentRepository.existsByFilenameAndUser(docInfo.filename, user)) throw DocumentAlreadyExistsException()

        val newDoc = Document()
        newDoc.user = user
        newDoc.filename = docInfo.filename
        newDoc.amount = docInfo.amount
        newDoc.year = docInfo.year
        newDoc.description = docInfo.description
        newDoc.tag = ETag.valueOf(docInfo.tag.uppercase(Locale.getDefault()))
        val session = entityManagerFactory.createEntityManager().delegate as Session
        newDoc.file = session.lobHelper.createBlob(docFile.inputStream, docFile.size)
        documentRepository.save(newDoc)
    }

    @Transactional
    fun removeDocument(user: User, id: Int) {
        if (!documentRepository.existsById(id)) throw DocumentNotFoundException()
        val document = documentRepository.findById(id).get()
        if (document.user?.id != user.id) throw DocumentBelongsToAnotherUserException()
        documentRepository.removeById(id)
    }

    @Transactional
    fun updateDocument(user: User, docInfo: DocumentDto, docFile: MultipartFile) {
        if (!docInfo.filename.matches(Regex("^[_A-Za-z0-9]{3,}\\.(?:pdf|doc|docx)\$"))) throw FilenameNotValidException()
        if (docInfo.amount < 0) throw AmountIsNegativeException()
        if (docInfo.year < 1950 || docInfo.year > LocalDate.now().year) throw YearNotValidException()
        if (docInfo.id == null) throw DocumentNotFoundException()
        if (!ETag.values().any { it.name == docInfo.tag.uppercase(Locale.getDefault()) }) throw TagNotFoundException()
        if (!documentRepository.existsById(docInfo.id)) throw DocumentNotFoundException()
        val document = documentRepository.findById(docInfo.id).get()
        if (document.user?.id != user.id) throw DocumentBelongsToAnotherUserException()
        if (docInfo.filename != document.filename && documentRepository.existsByFilenameAndUser(docInfo.filename, user))
            throw DocumentWithSameFilenameSameUserAlreadyExistsException()

        document.filename = docInfo.filename
        document.amount = docInfo.amount
        document.year = docInfo.year
        document.description = docInfo.description
        document.tag = ETag.valueOf(docInfo.tag.uppercase(Locale.getDefault()))
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
        if (document.user?.id != user.id) throw DocumentBelongsToAnotherUserException()
        return DocumentFileDto(document.filename!!, document.file?.length()!!, document.file?.binaryStream!!)
    }

    @Transactional
    fun report(user: User, year: Int): Map<ETag, Float> {
        if (year < 1950 || year > LocalDate.now().year) throw YearNotValidException()
        val report : MutableMap<ETag, Float> = mutableMapOf()
        for (tag in ETag.values()) {
            val docList = documentRepository.findByUserAndYearAndTag(user, year, tag)
            var total = 0.00f
            for (doc in docList)
                total += doc.amount!!
            report[tag] = total
        }
        return report.toMap()
    }

    @Transactional
    fun showAllUserDocuments(user: User): List<DocumentDto> {
        val docList = documentRepository.findByUser(user)
        if (docList.isEmpty()) return emptyList()
        return docList.map { doc -> doc.toDto() }
    }

    @Transactional
    fun showUserDocumentsByTag(user: User, tag: String): List<DocumentDto> {
        if (!ETag.values().any { it.name == tag.uppercase(Locale.getDefault()) }) throw TagNotFoundException()
        val docList = documentRepository.findByUserAndTag(user, ETag.valueOf(tag.uppercase(Locale.getDefault())))
        if (docList.isEmpty()) return emptyList()
        return docList.map { doc -> doc.toDto() }
    }

    @Transactional
    fun showUserDocumentsByYear(user: User, year: Int): List<DocumentDto> {
        if (year < 1950 || year > LocalDate.now().year) throw YearNotValidException()
        val docList = documentRepository.findByUserAndYear(user, year)
        if (docList.isEmpty()) return emptyList()
        return docList.map { doc -> doc.toDto() }
    }

    @Transactional
    fun showUserDocumentsByYearAndTag(user: User, year: Int, tag: String): List<DocumentDto> {
        if (year < 1950 || year > LocalDate.now().year) throw YearNotValidException()
        if (!ETag.values().any { it.name == tag.uppercase(Locale.getDefault()) }) throw TagNotFoundException()
        val docList = documentRepository.findByUserAndYearAndTag(user, year, ETag.valueOf(tag.uppercase(Locale.getDefault())))
        if (docList.isEmpty()) return emptyList()
        return docList.map { doc -> doc.toDto() }
    }
}