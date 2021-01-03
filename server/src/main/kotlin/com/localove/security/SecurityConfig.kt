package com.localove.security

import com.localove.security.jwt.JwtAuthenticationProvider
import com.localove.security.jwt.JwtRequestFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class SecurityConfig(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val jwtRequestFilter: JwtRequestFilter,
    private val exceptionHandlerFilter: ExceptionHandlerFilter
) : WebSecurityConfigurerAdapter() {

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(jwtAuthenticationProvider)
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors().configurationSource(corsConfigurationSource)
            .and()
            .httpBasic().disable()
            .addFilterBefore(jwtRequestFilter, AbstractPreAuthenticatedProcessingFilter::class.java)
            .addFilterBefore(exceptionHandlerFilter, JwtRequestFilter::class.java)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .anonymous()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//            .antMatchers("/profile").hasAuthority("DEFAULT")
//            .antMatchers("/api/**").hasAuthority("DEFAULT")
//            .antMatchers("/api/v1/users/**").hasAuthority("ADMIN")
    }

    private val corsConfigurationSource: CorsConfigurationSource =
        UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration(
                "/**",
                CorsConfiguration()
                    .applyPermitDefaultValues()
                    .apply { allowedMethods = listOf("*") }
            )
        }
}