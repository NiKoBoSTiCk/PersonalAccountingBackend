package sdcc.accounting.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sdcc.accounting.dto.LoginDto
import sdcc.accounting.dto.SignupDto
import sdcc.accounting.dto.TokenDto
import sdcc.accounting.services.HashService
import sdcc.accounting.services.TokenService
import sdcc.accounting.exceptions.LoginFailedException
import sdcc.accounting.exceptions.UserAlreadyExistException
import sdcc.accounting.services.UserService
import kotlin.jvm.Throws

@RestController
@RequestMapping("/api")
class AuthController(
    private val hashService: HashService,
    private val tokenService: TokenService,
    private val userService: UserService,
) {
    @PostMapping("/login")
    @Throws(LoginFailedException::class)
    fun login(@RequestBody payload: LoginDto): TokenDto {
        val user = userService.login(payload.email, payload.password)?: throw LoginFailedException()

        return TokenDto(
            tokenService.createToken(user),
            user.username!!,
            user.email!!
        )
    }

    @PostMapping("/signup")
    @Throws(UserAlreadyExistException::class)
    fun signup(@RequestBody payload: SignupDto): TokenDto {
        val user = userService.signup(payload.email, payload.username, payload.password)?: throw UserAlreadyExistException()

        return TokenDto(
            tokenService.createToken(user),
            user.username!!,
            user.email!!
        )
    }
}