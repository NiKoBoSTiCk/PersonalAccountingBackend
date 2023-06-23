package sdcc.accounting.repositories

import org.springframework.data.jpa.repository.JpaRepository
import sdcc.accounting.model.User

interface UserRepository : JpaRepository<User, Int> {

    fun existsByEmail(email: String): Boolean

    fun existsByUsername(username: String): Boolean

    fun findByEmail(email: String): User?
}