package sdcc.accounting.payloads.requests

data class LoginRequest(
    val email: String,
    val password: String,
)