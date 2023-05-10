package sdcc.accounting.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import sdcc.accounting.payload.request.DocumentRequest
import sdcc.accounting.services.DocumentService
import sdcc.accounting.support.exceptions.DocumentNotFoundException
import sdcc.accounting.support.exceptions.TagNotFoundException
import sdcc.accounting.support.exceptions.UserNotFoundException
import java.time.Year
import kotlin.jvm.Throws

@RestController
@RequestMapping("/documents")
class DocumentController(private val documentService: DocumentService) {

    @PostMapping
    @Throws(UserNotFoundException::class, TagNotFoundException::class)
    fun create(@RequestBody doc: DocumentRequest): ResponseEntity<Any> {
        documentService.addDocument(doc)
        return ResponseEntity.ok("Added successful!")
    }

    @DeleteMapping
    @Throws(DocumentNotFoundException::class)
    fun delete(@RequestBody id: Int): ResponseEntity<Any> {
        documentService.removeDocument(id)
        return ResponseEntity.ok("Removed successful!")
    }

    @PutMapping
    @Throws(UserNotFoundException::class, TagNotFoundException::class, DocumentNotFoundException::class)
    fun update(@RequestBody doc: DocumentRequest): ResponseEntity<Any> {
        documentService.updateDocument(doc)
        return ResponseEntity.ok("Updated successful!")
    }

    @GetMapping
    @Throws(UserNotFoundException::class, TagNotFoundException::class, DocumentNotFoundException::class)
    fun getDocumentsByYearAndTag(@RequestParam(value = "email") email: String,
                                 @RequestParam(value = "year") year: Year,
                                 @RequestParam(value = "tag") tag: String) : ResponseEntity<Any> {
        return ResponseEntity.ok(documentService.showDocumentByUserAndYearAndTag(email, year, tag))
    }
}