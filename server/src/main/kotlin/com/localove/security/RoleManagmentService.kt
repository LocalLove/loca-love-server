package com.localove.security

import com.localove.security.entities.Role
import com.localove.security.entities.RoleRepository
import com.localove.security.entities.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoleManagementService(
    private val roleRepository: RoleRepository
) {

    @Transactional
    fun addRoleToUser(user: User, roleName: Role.Name) =
        doActionWithUserRole(user, roleName) { roles.add(it) }

    @Transactional
    fun removeRoleFromUser(user: User, roleName: Role.Name) =
        doActionWithUserRole(user, roleName) { roles.remove(it) }

    private inline fun doActionWithUserRole(
        user: User, roleName: Role.Name, action: (User).(Role) -> Unit
    ) {
        user.action(findRoleByName(roleName))
    }

    private fun findRoleByName(roleName: Role.Name) =
        roleRepository.findByName(roleName)
            ?: throw IllegalArgumentException("DB doesn't contain predefined role: UNCONFIRMED")
}

