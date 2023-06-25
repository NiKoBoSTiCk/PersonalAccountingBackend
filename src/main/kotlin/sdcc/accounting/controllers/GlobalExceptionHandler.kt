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
            FilenameNotValidException::class
    )
    fun handleException(ex: Exception): ResponseEntity<Any> {
        if (ex is LoginFailedException)
            return ResponseEntity.status(401).build()
        if (ex is UserNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is TagNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is DocumentNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is UserAlreadyExistsException)
            return ResponseEntity.badRequest().build()
        if (ex is FilenameNotValidException)
            return ResponseEntity.badRequest().build()
        if (ex is DocumentAlreadyExistsException)
            return ResponseEntity.badRequest().build()
        if (ex is DocumentWithSameFilenameSameUserAlreadyExistsException)
            return ResponseEntity.badRequest().build()
        if (ex is DocumentFileIsEmptyException)
            return ResponseEntity.badRequest().build()
        if (ex is TagNotValidException)
            return ResponseEntity.badRequest().build()
        if (ex is DocumentBelongsToAnotherUserException)
            return ResponseEntity.badRequest().build()
        if (ex is AmountIsNegativeException)
            return ResponseEntity.badRequest().build()
        if (ex is InvalidBearerTokenException)
            return ResponseEntity.badRequest().build()
        if (ex is YearNotValidException)
            return ResponseEntity.badRequest().build()
        return if (ex is JwtParseException)
            ResponseEntity.badRequest().build()
        else
            ResponseEntity.internalServerError().body(ex.stackTrace)
    }
}