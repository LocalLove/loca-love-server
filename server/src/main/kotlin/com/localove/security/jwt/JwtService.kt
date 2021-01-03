package com.localove.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.time.Clock
import java.util.*

data class JwtPayload(val userId: Int)

@Component
class JwtService(
    private val clock: Clock,

    private val properties: JwtProperties
) {
    private val key = Keys.hmacShaKeyFor(properties.secret.toByteArray())

    private val parser = Jwts.parserBuilder().setSigningKey(key).build()

    private fun parseClaims(token: String): Claims {
        return parser.parseClaimsJwt(token).body
    }

    fun generateToken(userId: Int): String {
        return Jwts.builder().apply {
            setSubject(userId.toString())
            clock.millis().let {
                setIssuedAt(Date(it))
                setExpiration(Date(it + properties.expiration.toMillis()))
            }
            signWith(key)
        }.compact()
    }

    fun getPayload(token: String): JwtPayload {
        // без валидации, т.к. parseClaimsJwt это делает за нас
        return JwtPayload(
            parseClaims(token).subject.toInt()
        )
    }
}