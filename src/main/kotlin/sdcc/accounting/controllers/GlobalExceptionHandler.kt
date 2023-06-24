package sdcc.accounting.controllers

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import sdcc.accounting.exceptions.*

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
            UserNotFoundException::class,
            TagNotFoundException::class,
            DocumentNotFoundException::class,
            LoginFailedException::class,
            UserAlreadyExistException::class,
            TagNotValidException::class,
            UserNotValidException::class,
            NegativeAmountException::class,
            YearNotValidException::class,
            InvalidBearerTokenException::class,
            DocumentWithSameNameAlreadyExistsException::class,
            EmptyDocumentException::class,
            FilenameNotValidException::class
    )
    fun handleException(ex: Exception): ResponseEntity<Any> {
        if (ex is FilenameNotValidException)
            return ResponseEntity.badRequest().build()
        if (ex is DocumentAlreadyExistsException)
            return ResponseEntity.badRequest().build()
        if (ex is DocumentWithSameNameAlreadyExistsException)
            return ResponseEntity.badRequest().build()
        if (ex is EmptyDocumentException)
            return ResponseEntity.badRequest().build()
        if (ex is UserNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is TagNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is DocumentNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is LoginFailedException)
            return ResponseEntity.status(401).build()
        if (ex is UserAlreadyExistException)
            return ResponseEntity.badRequest().build()
        if (ex is TagNotValidException)
            return ResponseEntity.badRequest().build()
        if (ex is UserNotValidException)
            return ResponseEntity.badRequest().build()
        if (ex is NegativeAmountException)
            return ResponseEntity.badRequest().build()
        if (ex is InvalidBearerTokenException)
            return ResponseEntity.badRequest().body("Not Auth")
        if (ex is YearNotValidException)
            return ResponseEntity.badRequest().build()
        if (ex is JwtParseException)
            return ResponseEntity.status(666).build()
        else
            return ResponseEntity.internalServerError().body(ex.stackTrace)
    }
}