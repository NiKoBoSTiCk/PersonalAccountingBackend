package sdcc.accounting.payloads.responses

class TokenResponse(
    var accessToken: String,
    var id: Int,
    var username: String,
    var email: String,
) { var tokenType = "Bearer" }