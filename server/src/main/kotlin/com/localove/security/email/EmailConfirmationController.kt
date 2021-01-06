package com.localove.security.email

import com.localove.api.ErrorType
import com.localove.security.RoleManagementService
import com.localove.security.entities.Role
import com.localove.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
internal class EmailConfirmationController(
    private val emailTokenService: EmailTokenService,
    private val roleManagementService: RoleManagementService
) {

    @PostMapping("/confirm")
    fun confirmEmail(@RequestParam("token") token: String): ResponseEntity<*> {
        val validationResult = emailTokenService.validateToken(token)
        if (validationResult !is EmailTokenValidationResult.Success) {
            return Response.error(
                errorType = ErrorType.INVALID_TOKEN,
                message = "Invalid token"
            )
        }

        val userId = validationResult.user.id!!
        roleManagementService.removeRoleFromUser(userId, Role.Name.UNCONFIRMED)
        roleManagementService.addRoleToUser(userId, Role.Name.NEWCOMER)
        return Response.ok()
    }

}