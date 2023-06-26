package sdcc.accounting.dto

data class TokenDto(
    var token: String,
    var username: String,
    var email: String,
    val tokenType: String = "Bearer",
)