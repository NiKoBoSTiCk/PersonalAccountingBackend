package sdcc.accounting.repositories

import org.springframework.data.jpa.repository.JpaRepository
import sdcc.accounting.entities.User

interface UserRepository : JpaRepository<User, Int> {

    fun existsByUsernameOrEmail(username: String, email: String): Boolean

    fun findByEmail(email: String): User?

}