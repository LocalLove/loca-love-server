package com.localove.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class EmailConfig(
    @Value("\${email.email-confirmation-url}")
    val emailConfirmationUrl: String,

    @Value("\${email.password-restore-url}")
    val passwordRestoreUrl: String,

    @Value("\${email.templates.email-confirmation-template-file-path}")
    val emailConfirmationTemplateFilePath: String,

    @Value("\${email.templates.password-restore-template-file-path}")
    val passwordRestoreTemplateFilePath: String
)