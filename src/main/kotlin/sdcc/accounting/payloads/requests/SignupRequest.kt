package sdcc.accounting.payloads.requests

data class SignupRequest(
    val username: String,
    val email: String,
    val password: String,
)