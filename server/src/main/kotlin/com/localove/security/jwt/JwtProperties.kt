package com.localove.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.convert.DurationUnit
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.temporal.ChronoUnit

@ConfigurationProperties("loca-love.jwt")
@Component
class JwtProperties {
    lateinit var secret: String

    @DurationUnit(ChronoUnit.HOURS)
    lateinit var expiration: Duration
}