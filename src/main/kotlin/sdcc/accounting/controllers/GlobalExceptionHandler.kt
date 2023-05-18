package sdcc.accounting.controllers

import org.springframework.http.ResponseEntity
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
        InvalidTagException::class,
        InvalidUserException::class,
        NegativeAmountException::class,
        NullDocumentException::class,
        YearNotValidException::class
    )
    fun handleException(ex: Exception): ResponseEntity<Any> {
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
        if (ex is InvalidTagException)
            return ResponseEntity.badRequest().build()
        if (ex is InvalidUserException)
            return ResponseEntity.badRequest().build()
        if (ex is NegativeAmountException)
            return ResponseEntity.badRequest().build()
        if (ex is NullDocumentException)
            return ResponseEntity.badRequest().build()
        return if (ex is YearNotValidException) ResponseEntity.badRequest().build()
        else ResponseEntity.internalServerError().body(ex.stackTrace)
    }
}