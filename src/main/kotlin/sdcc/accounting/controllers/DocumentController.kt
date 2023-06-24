package sdcc.accounting.controllers

import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import sdcc.accounting.config.toUser
import sdcc.accounting.dto.DocumentDto
import sdcc.accounting.exceptions.*
import sdcc.accounting.services.DocumentService
import java.io.InputStream


@RestController
@RequestMapping("/api/documents")
class DocumentController(private val documentService: DocumentService) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun createDocument(auth: Authentication,
                       @RequestPart("docInfo") docInfo: DocumentDto,
                       @RequestPart("docFile") docFile: MultipartFile): ResponseEntity<Any> {
        documentService.addDocument(auth.toUser(), docInfo, docFile)
        return ResponseEntity.ok("Document added successfully!")
    }

    @DeleteMapping
    fun deleteDocument(auth: Authentication, @RequestParam(value = "id") id: Int): ResponseEntity<Any> {
        val user = auth.toUser()
        documentService.removeDocument(auth.toUser(), id)
        return ResponseEntity.ok("Document removed successfully!")
    }

    @PutMapping(consumes = ["multipart/form-data"])
    fun updateDocument(auth: Authentication,
                       @RequestPart("docInfo") docInfo: DocumentDto,
                       @RequestPart("docFile") docFile: MultipartFile): ResponseEntity<Any> {
        documentService.updateDocument(auth.toUser(), docInfo, docFile)
        return ResponseEntity.ok("Document updated successfully!")
    }

    @GetMapping
    @ResponseBody
    fun downloadDocument(auth: Authentication, @RequestParam(value = "id") id: Int): ResponseEntity<InputStreamResource> {
        val docFile = documentService.downloadDocument(auth.toUser(), id)
        val ext = docFile?.file?.extension
        val contentType = if (ext == "pdf") MediaType.APPLICATION_PDF else MediaType.APPLICATION_OCTET_STREAM
        val inputStream = docFile?.file?.inputStream() as InputStream
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(InputStreamResource(inputStream))
    }

    @GetMapping("/report")
    fun getReport(auth: Authentication) {
        //TODO
    }

    @GetMapping("/all")
    fun getAllDocuments(auth: Authentication) : ResponseEntity<Any> {
        return ResponseEntity.ok(documentService.showAllUserDocuments(auth.toUser()))
    }

    @GetMapping("/by_tag")
    @Throws(TagNotFoundException::class)
    fun getDocumentsByTag(auth: Authentication, @RequestParam(value = "tag") tag: String) : ResponseEntity<Any> {
        return ResponseEntity.ok(documentService.showUserDocumentsByTag(auth.toUser(), tag))
    }

    @GetMapping("/by_year")
    fun getDocumentsByYear(auth: Authentication, @RequestParam(value = "year") year: Int) : ResponseEntity<Any> {
        return ResponseEntity.ok(documentService.showUserDocumentsByYear(auth.toUser(), year))
    }

    @GetMapping("/by_year_and_tag")
    fun getDocumentsByYearAndTag(auth: Authentication,
                                 @RequestParam(value = "year") year: Int,
                                 @RequestParam(value = "tag") tag: String) : ResponseEntity<Any> {
        return ResponseEntity.ok(documentService.showUserDocumentsByYearAndTag(auth.toUser(), year, tag))
    }
}