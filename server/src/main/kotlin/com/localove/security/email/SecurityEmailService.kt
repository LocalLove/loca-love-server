package com.localove.security.email

import com.localove.email.EmailService
import com.localove.util.LoggerProperty
import com.localove.util.ResourceLoader
import org.springframework.stereotype.Service
import java.util.*

@Service
class SecurityEmailService(
    private val emailService: EmailService,

    private val config: EmailConfig
) {
    private val templateResolver = MessageTemplateResolver("{", "}")

    private val log by LoggerProperty()

    fun sendEmailConfirmation(email: String, uuid: UUID) {
        log.trace("Trying to send email confirmation message to $email")

        val template = ResourceLoader.loadAsString(config.emailConfirmationTemplateFilePath)
        val substitutions = mapOf(
            "url" to config.emailConfirmationUrl, "name" to email, "uuid" to uuid.toString()
        )
        val body = templateResolver.resolve(template, substitutions)

        emailService.sendMessage(email, "Подтверждение почты", body)
    }

    fun sendPasswordRestore(email: String, uuid: UUID) {
        log.trace("Trying to send password restore message to $email")

        val template = ResourceLoader.loadAsString(config.passwordRestoreTemplateFilePath)
        val substitutions = mapOf(
            "url" to config.passwordRestoreUrl, "name" to email, "uuid" to uuid.toString()
        )
        val body = templateResolver.resolve(template, substitutions)

        emailService.sendMessage(email, "Сброс пароля", body)
    }
}

private class MessageTemplateResolver(
    private val prefix: String,

    private val postfix: String = ""
) {
    fun resolve(template: String, substitutions: Map<String, String>): String =
        substitutions.entries.fold(template) {
                acc, (argName, value) -> acc.replace(getPlaceholderRegex(argName), value)
        }

    private fun getPlaceholderRegex(argName: String) = prefix + argName + postfix
}