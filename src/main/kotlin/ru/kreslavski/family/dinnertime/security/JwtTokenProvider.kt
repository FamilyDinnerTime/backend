package ru.kreslavski.family.dinnertime.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {

    @Value("\${app.jwt-secret:defaultSecretKey}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwt-expiration-milliseconds:86400000}")
    private var jwtExpirationInMs: Long = 86400000

    private val key: Key by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetails
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .subject(userPrincipal.username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key as SecretKey)
            .compact()
    }

    fun getUsernameFromJWT(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(key as SecretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }

    fun validateToken(authToken: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(key as SecretKey)
                .build()
                .parseSignedClaims(authToken)
            true
        } catch (ex: Exception) {
            false
        }
    }
}
