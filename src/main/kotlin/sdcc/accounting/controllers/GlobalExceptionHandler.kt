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
        UserAlreadyExistsException::class,
        TagNotValidException::class,
        DocumentBelongsToAnotherUserException::class,
        AmountIsNegativeException::class,
        YearNotValidException::class,
        InvalidBearerTokenException::class,
        DocumentWithSameFilenameSameUserAlreadyExistsException::class,
        DocumentFileIsEmptyException::class,
        FilenameNotValidException::class,
        JwtParseException::class
    )
    fun handleException(ex: Exception): ResponseEntity<Any> {
        if (ex is LoginFailedException)
            return ResponseEntity.status(401).body("Login failed, wrong credential.")
        if (ex is UserNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is TagNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is DocumentNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is UserAlreadyExistsException)
            return ResponseEntity.badRequest().body("Signup failed, user already exists.")
        if (ex is JwtParseException)
            return ResponseEntity.badRequest().body("Jwt parse error.")
        if (ex is FilenameNotValidException)
            return ResponseEntity.badRequest().body("Filename is not valid.")
        if (ex is DocumentAlreadyExistsException)
            return ResponseEntity.badRequest().body("Document already exists.")
        if (ex is DocumentWithSameFilenameSameUserAlreadyExistsException)
            return ResponseEntity.badRequest().body("Two documents with same filename and same user.")
        if (ex is DocumentFileIsEmptyException)
            return ResponseEntity.badRequest().body("Cannot create an empty document.")
        if (ex is TagNotValidException)
            return ResponseEntity.badRequest().body("Tag is not valid.")
        if (ex is DocumentBelongsToAnotherUserException)
            return ResponseEntity.badRequest().body("Access denied.")
        if (ex is AmountIsNegativeException)
            return ResponseEntity.badRequest().body("The amount is negative.")
        if (ex is InvalidBearerTokenException)
            return ResponseEntity.badRequest().body("Token is not valid.")
        if (ex is YearNotValidException)
            return ResponseEntity.badRequest().body("Year is not valid.")
        return ResponseEntity.internalServerError().body(ex.stackTrace)
    }
}