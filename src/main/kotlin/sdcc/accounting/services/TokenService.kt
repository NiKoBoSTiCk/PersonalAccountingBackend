package sdcc.accounting.services

import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import sdcc.accounting.exceptions.JwtParseException
import sdcc.accounting.model.User
import sdcc.accounting.repositories.UserRepository
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val userRepository: UserRepository,
) {
    fun createToken(user: User): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
            .subject(user.email)
            .claim("email", user.email)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun parseToken(token: String): User {
        val jwt = jwtDecoder.decode(token)
        try {
            val email = jwt.claims["email"] as String
            return userRepository.findByEmail(email)!!
        } catch (e: Exception) {
            throw JwtParseException()
        }
    }
}