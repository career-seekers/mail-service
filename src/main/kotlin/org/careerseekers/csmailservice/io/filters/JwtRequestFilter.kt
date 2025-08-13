package org.careerseekers.csmailservice.io.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.careerseekers.csmailservice.exceptions.GrpcServiceUnavailableException
import org.careerseekers.csmailservice.io.BasicErrorResponse
import org.careerseekers.csmailservice.utils.JwtUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.naming.AuthenticationException
import kotlin.text.startsWith
import kotlin.text.substring
import kotlin.toString

@Component
class JwtRequestFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    private val json = Json { encodeDefaults = true; prettyPrint = false }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authorizationHeader = request.getHeader("Authorization")

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                val jwtToken = authorizationHeader.substring(7)

                if (jwtUtil.verifyToken(jwtToken)) {
                    val user = jwtUtil.getUserFromToken(jwtToken)
                    val authorities = listOf(SimpleGrantedAuthority(user?.role.toString()))

                    if (SecurityContextHolder.getContext().authentication == null) {
                        val authReq = UsernamePasswordAuthenticationToken(user?.email, null, authorities)
                        SecurityContextHolder.getContext().authentication = authReq
                    }
                }
            }

            filterChain.doFilter(request, response)

        } catch (ex: AuthenticationException) {
            SecurityContextHolder.clearContext()
            respondWithError(response, HttpServletResponse.SC_UNAUTHORIZED, ex.message ?: "Authentication error")

        } catch (ex: GrpcServiceUnavailableException) {
            SecurityContextHolder.clearContext()
            respondWithError(response, HttpServletResponse.SC_SERVICE_UNAVAILABLE, ex.message ?: "Service unavailable")

        } catch (ex: Exception) {
            SecurityContextHolder.clearContext()
            respondWithError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.message ?: "Internal server error")
        }
    }

    private fun respondWithError(response: HttpServletResponse, status: Int, message: String) {
        response.status = status
        response.contentType = "application/json"
        val errorResponse = BasicErrorResponse(status = status, message = message)

        response.writer.write(json.encodeToString(errorResponse))
    }
}
