package com.localove.security

import com.localove.security.entities.User
import org.springframework.security.core.context.SecurityContextHolder

internal class AuthorizedUserInfo {

    companion object {

        fun getPrincipal(): User =
            SecurityContextHolder
                .getContext()
                .authentication
                .principal as User

    }

}