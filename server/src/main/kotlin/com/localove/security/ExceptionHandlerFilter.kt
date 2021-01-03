package com.localove.security

import com.localove.exceptions.NotFoundException
import com.localove.util.LoggerProperty
import io.jsonwebtoken.JwtException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {
    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (exc: Exception) {
            when (exc) {
                is IllegalArgumentException,
                is JwtException,
                is ArrayIndexOutOfBoundsException,
                is NotFoundException,
                is NumberFormatException -> {
                    logger.error("Authorization error during http filtering", exc)
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                }
                else -> {
                    logger.error("Error during http filtering", exc)
                    response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                }
            }
        }
    }
}