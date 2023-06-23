package sdcc.accounting.dto

data class TokenDto(
    var token: String,
    var username: String,
    var email: String,
) { var tokenType = "Bearer" }