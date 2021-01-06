package com.localove.security.email

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("email")
class EmailConfig {
    lateinit var emailConfirmationUrl: String

    lateinit var passwordRestoreUrl: String

    lateinit var emailConfirmationTemplateFilePath: String

    lateinit var passwordRestoreTemplateFilePath: String
}