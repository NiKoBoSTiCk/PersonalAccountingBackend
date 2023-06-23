package sdcc.accounting.services

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import sdcc.accounting.exceptions.LoginFailedException
import sdcc.accounting.exceptions.UserAlreadyExistException
import sdcc.accounting.model.User
import sdcc.accounting.repositories.UserRepository
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository,
    private val hashService: HashService
) {

    @Transactional
    fun login(email: String, password: String): User? {
       val user = userRepository.findByEmail(email)?: throw LoginFailedException()
       if (!hashService.checkBcrypt(password, user.password!!)) throw LoginFailedException()
       return user
    }

    @Transactional
    fun signup(email: String, username: String, password: String): User? {
        if (userRepository.existsByEmail(email)) throw UserAlreadyExistException()
        if (userRepository.existsByUsername(username)) throw UserAlreadyExistException()

        val user = User()
        user.email = email
        user.username = username
        user.password = hashService.hashBcrypt(password)
        userRepository.save(user)
        return user
    }
}