package com.localove.security.jwt

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter : OncePerRequestFilter() {
    companion object {
        private const val AUTH_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        getJwt(request)?.let {
            SecurityContextHolder.getContext().authentication = JwtAuthentication(it)
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwt(request: HttpServletRequest): String? {
        return request.getHeader(AUTH_HEADER)?.let {
            return if (it.startsWith(BEARER_PREFIX))
                it.removePrefix(BEARER_PREFIX) else null
        }
    }
}