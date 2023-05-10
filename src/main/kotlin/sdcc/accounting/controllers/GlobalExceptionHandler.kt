package sdcc.accounting.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import sdcc.accounting.support.exceptions.DocumentNotFoundException
import sdcc.accounting.support.exceptions.TagNotFoundException
import sdcc.accounting.support.exceptions.UserNotFoundException

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
        UserNotFoundException::class,
        TagNotFoundException::class,
        DocumentNotFoundException::class
    )
    fun handleException(ex: Exception): ResponseEntity<Any> {
        if (ex is UserNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is TagNotFoundException)
            return ResponseEntity.notFound().build()
        if (ex is DocumentNotFoundException)
            return ResponseEntity.notFound().build()

        else return ResponseEntity.internalServerError().body(ex.stackTrace);
    }
}