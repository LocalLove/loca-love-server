package com.localove.security

import com.localove.security.entities.Role
import com.localove.security.entities.RoleRepository
import com.localove.security.entities.User
import com.localove.security.entities.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class RoleManagementService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {

    @Transactional
    fun addRoleToUser(userId: Long, roleName: Role.Name): RoleManagementResult =
        doActionWithUserRole(userId, roleName) { roles.add(it) }

    @Transactional
    fun removeRoleFromUser(userId: Long, roleName: Role.Name): RoleManagementResult =
        doActionWithUserRole(userId, roleName) { roles.remove(it) }

    private inline fun doActionWithUserRole(
        userId: Long, roleName: Role.Name, action: (User).(Role) -> Unit
    ): RoleManagementResult {
        val role = roleRepository.findByName(roleName)
        check(role != null) { "role must be not null" }
        val user = userRepository.findById(userId).orElse(null)
            ?: return RoleManagementResult.UserNotFound
        user.action(role)
        userRepository.save(user)
        return RoleManagementResult.Success
    }

}

internal sealed class RoleManagementResult {
    object Success: RoleManagementResult()
    object UserNotFound: RoleManagementResult()
}