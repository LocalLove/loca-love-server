package com.localove.security.jwt

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(
    val token: String
) : Authentication {
    override fun getAuthorities(): Collection<GrantedAuthority>? = null
    override fun getCredentials(): Any? = null
    override fun getDetails(): Any? = null
    override fun getName(): String? = null
    override fun getPrincipal(): Any? = null
    override fun isAuthenticated(): Boolean = false
    override fun setAuthenticated(b: Boolean) {}
}