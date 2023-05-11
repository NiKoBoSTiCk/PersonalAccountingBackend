package sdcc.accounting.config

import org.springframework.security.core.Authentication
import sdcc.accounting.entities.User

fun Authentication.toUser(): User {
    return principal as User
}