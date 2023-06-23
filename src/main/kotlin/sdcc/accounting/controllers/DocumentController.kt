package sdcc.accounting.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sdcc.accounting.services.DocumentService
import sdcc.accounting.config.toUser
import sdcc.accounting.dto.DocumentDto
import sdcc.accounting.model.Document
import sdcc.accounting.exceptions.*
import kotlin.jvm.Throws

@RestController
@RequestMapping("/api/documents")
class DocumentController(private val documentService: DocumentService) {

    @PostMapping(consumes = ["multipart/form-data"])
    @Throws(InvalidTagException::class, YearNotValidException::class, NegativeAmountException::class)
    fun create(auth: Authentication,
               @RequestPart("docInfo") docInfo: DocumentDto,
               @RequestPart("docFile") docFile: MultipartFile): ResponseEntity<Any> {
        val user = auth.toUser()
        documentService.addDocument(user, docInfo, docFile)
        return ResponseEntity.ok("Added successful!")
    }

    @DeleteMapping
    @Throws(DocumentNotFoundException::class)
    fun delete(auth: Authentication, @RequestParam(value = "id") id: Int): ResponseEntity<Any> {
        val authUser = auth.toUser()
        documentService.removeDocument(authUser, id)
        return ResponseEntity.ok("Removed successful!")
    }

    @PutMapping
    @Throws(TagNotFoundException::class, DocumentNotFoundException::class)
    fun update(auth: Authentication, @RequestBody payload: Document): ResponseEntity<Any> {
        val authUser = auth.toUser()
        documentService.updateDocument(authUser, payload)
        return ResponseEntity.ok("Updated successful!")
    }

    @GetMapping("/all")
    fun getAllDocuments(auth: Authentication) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showAllDocuments(authUser))
    }

    @GetMapping("/by_tag")
    @Throws(TagNotFoundException::class)
    fun getDocumentsByTag(auth: Authentication, @RequestParam(value = "tag") tag: String) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showDocumentsByTag(authUser, tag))
    }

    @GetMapping("/by_year")
    fun getDocumentsByYear(auth: Authentication, @RequestParam(value = "year") year: Int) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showDocumentsByYear(authUser, year))
    }

    @GetMapping("/by_year_and_tag")
    @Throws(TagNotFoundException::class)
    fun getDocumentsByYearAndTag(auth: Authentication, @RequestParam(value = "year") year: Int,
                                 @RequestParam(value = "tag") tag: String) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showDocumentsByYearAndTag(authUser, year, tag))
    }
}