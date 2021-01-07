package com.localove.security.login

import com.localove.api.ErrorType
import com.localove.api.security.Credentials
import com.localove.exceptions.FirstStartConfigRequiredException
import com.localove.exceptions.UnconfirmedUserException
import com.localove.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val service: LoginService
) {
    @PostMapping("/sign-in")
    fun signIn(@RequestBody credentials: Credentials): ResponseEntity<*> {
        return try {
            Response.ok(service.signIn(credentials))
        } catch (exc: UnconfirmedUserException) {
            Response.error(ErrorType.UNCONFIRMED, "Email confirmation required")
        } catch (exc: FirstStartConfigRequiredException) {
            Response.error(ErrorType.FIRST_START_CONFIG_REQUIRED, exc.localizedMessage)
        }
    }
}