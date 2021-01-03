package com.localove.security.jwt

import com.localove.user.UserService
import com.localove.user.entities.User
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val jwtProvider: JwtService,
    private val userService: UserService
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication? {
        val jwt = (authentication as JwtAuthentication).token
        val payload = jwtProvider.getPayload(jwt)
        return UserAuthentication(
            userService.findById(payload.userId)
        )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return JwtAuthentication::class.java == authentication
    }
}

class UserAuthentication(
    private val principal: User
) : Authentication {
    override fun getPrincipal() = principal
    override fun getAuthorities(): Collection<GrantedAuthority> =
        principal.roles.map { SimpleGrantedAuthority(it.name.name) }

    override fun setAuthenticated(isAuthenticated: Boolean) {}
    override fun getName(): String = principal.login
    override fun getCredentials(): Any = principal.password
    override fun isAuthenticated(): Boolean = true
    override fun getDetails(): Any? = null
}