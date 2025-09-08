package org.careerseekers.csmailservice.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.careerseekers.csmailservice.cache.UsersCacheClient
import org.careerseekers.csmailservice.config.JwtProperties
import org.careerseekers.csmailservice.dto.UsersCacheDto
import org.careerseekers.csmailservice.exceptions.JwtAuthenticationException
import org.careerseekers.csmailservice.exceptions.NotFoundException
import java.util.Date
import kotlin.text.toByteArray

@Utility
class JwtUtil(
    private val usersCacheClient: UsersCacheClient,
    private val jwtProperties: JwtProperties
) {
    fun verifyToken(token: String, throwTimeLimit: Boolean = true): Boolean {
        val claims = getClaims(token) ?: throw JwtAuthenticationException("Invalid token claims")
        if (!claims.expiration.after(Date()) && throwTimeLimit) {
            throw JwtAuthenticationException("Token expired")
        }

        return true
    }

    fun getUserFromToken(token: String): UsersCacheDto? {
        val claims = getClaims(token)

        return usersCacheClient.getItemFromCache((claims?.get("id") as Int).toLong())
            ?: throw NotFoundException("User with id ${claims["id"]} not found")
    }

    fun getClaims(token: String): Claims? {
        val claims = try {
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray()))
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (_: ExpiredJwtException) {
            throw JwtAuthenticationException("Jwt token expired")
        }

        return claims
    }

}