package sdcc.accounting.dto

data class LoginDto(
        val email: String,
        val password: String
)

data class SignupDto(
        val username: String,
        val email: String,
        val password: String,
)


