package sdcc.accounting.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sdcc.accounting.entities.User
import sdcc.accounting.payloads.requests.LoginRequest
import sdcc.accounting.payloads.requests.SignupRequest
import sdcc.accounting.payloads.responses.TokenResponse
import sdcc.accounting.repositories.UserRepository
import sdcc.accounting.services.HashService
import sdcc.accounting.services.TokenService
import sdcc.accounting.exceptions.LoginFailedException
import sdcc.accounting.exceptions.UserAlreadyExistException
import kotlin.jvm.Throws

@RestController
@RequestMapping("/auth")
class AuthController(
    private val hashService: HashService,
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
) {
    @PostMapping("/login")
    @Throws(LoginFailedException::class)
    fun login(@RequestBody payload: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(payload.email)?: throw LoginFailedException()
        if (!hashService.checkBcrypt(payload.password, user.password!!)) throw LoginFailedException()
        return TokenResponse(tokenService.createToken(user))
    }

    @PostMapping("/signup")
    @Throws(UserAlreadyExistException::class)
    fun signup(@RequestBody payload: SignupRequest): TokenResponse {
        if (userRepository.existsByUsernameOrEmail(payload.username, payload.email))
            throw UserAlreadyExistException()
        val user = User()
        user.username = payload.username
        user.email = payload.email
        user.password = hashService.hashBcrypt(payload.password)
        val savedUser = userRepository.save(user)
        return TokenResponse(tokenService.createToken(savedUser))
    }
}