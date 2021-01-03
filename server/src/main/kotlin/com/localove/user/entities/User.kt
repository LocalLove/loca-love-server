package com.localove.user.entities

import com.localove.Identifiable
import com.localove.security.role.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Users")
// добавил для проверки работы фильтра
// TODO: добавить остальные поля
class User(
    var name: String,
    var login: String,
    var email: String,
    var password: String,
) : Identifiable() {
    @ManyToMany
    @JoinTable(
        name = "UsersRoles",
        joinColumns = [JoinColumn(name = "userId")],
        inverseJoinColumns = [JoinColumn(name = "roleId")]
    )
    val roles: MutableSet<Role> = hashSetOf()
}

@Repository
interface UserRepository: JpaRepository<User, Int> {
    fun findByLoginOrEmail(login: String, email: String): Optional<User>

    fun existsByEmail(email: String): Boolean

    fun existsByLogin(login: String): Boolean
}