package sdcc.accounting.payloads.responses

data class TokenResponse(
    var token: String,
    var id: Int,
    var username: String,
    var email: String,
) { var tokenType = "Bearer" }