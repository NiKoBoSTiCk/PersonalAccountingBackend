package sdcc.accounting.repositories;

import org.springframework.data.jpa.repository.JpaRepository
import sdcc.accounting.entities.User
import java.util.*

interface UserRepository : JpaRepository<User, Int> {

    fun findUserByEmail(email: String): User?

    fun findUserByUsername(username: String): User?
}