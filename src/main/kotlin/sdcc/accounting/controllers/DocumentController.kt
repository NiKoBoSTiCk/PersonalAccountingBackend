package sdcc.accounting.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import sdcc.accounting.payloads.requests.DocumentRequest
import sdcc.accounting.services.DocumentService
import sdcc.accounting.config.toUser
import sdcc.accounting.exceptions.DocumentNotFoundException
import sdcc.accounting.exceptions.TagNotFoundException
import java.time.Year
import kotlin.jvm.Throws

@RestController
@RequestMapping("/documents")
class DocumentController(private val documentService: DocumentService) {

    @PostMapping
    @Throws(TagNotFoundException::class)
    fun create(auth: Authentication, @RequestBody payload: DocumentRequest): ResponseEntity<Any> {
        val authUser = auth.toUser()
        documentService.addDocument(authUser, payload)
        return ResponseEntity.ok("Added successful!")
    }

    @DeleteMapping
    @Throws(DocumentNotFoundException::class)
    fun delete(auth: Authentication, @RequestBody payload: DocumentRequest): ResponseEntity<Any> {
        val authUser = auth.toUser()
        documentService.removeDocument(authUser, payload)
        return ResponseEntity.ok("Removed successful!")
    }

    @PutMapping
    @Throws(TagNotFoundException::class, DocumentNotFoundException::class)
    fun update(auth: Authentication, @RequestBody payload: DocumentRequest): ResponseEntity<Any> {
        val authUser = auth.toUser()
        documentService.updateDocument(authUser, payload)
        return ResponseEntity.ok("Updated successful!")
    }

    @GetMapping("/all")
    fun getAllDocuments(auth: Authentication) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showAllDocuments(authUser))
    }

    @GetMapping("/by_tag/{tag}")
    @Throws(TagNotFoundException::class)
    fun getDocumentsByTag(auth: Authentication, @PathVariable(value = "tag") tag: String) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showDocumentsByTag(authUser, tag))
    }

    @GetMapping("/by_year/{year}")
    fun getDocumentsByYear(auth: Authentication, @PathVariable(value = "year") year: Year) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showDocumentsByYear(authUser, year))
    }

    @GetMapping("/by_year_and_tag/{year}/{tag}")
    @Throws(TagNotFoundException::class)
    fun getDocumentsByYearAndTag(auth: Authentication,
                                 @PathVariable(value = "year") year: Year,
                                 @PathVariable(value = "tag") tag: String) : ResponseEntity<Any> {
        val authUser = auth.toUser()
        return ResponseEntity.ok(documentService.showDocumentsByYearAndTag(authUser, year, tag))
    }
}