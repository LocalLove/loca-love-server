package com.localove.security.entities

import com.localove.Identifiable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users")
class User(

    @Column(name = "name")
    val name: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "login")
    var login: String,

    @Column(name = "password")
    var password: String

) : Identifiable() {

    @ManyToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = "users_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: MutableSet<Role> = hashSetOf()

}

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByLoginOrEmail(login: String, email: String): Optional<User>

    fun existsByEmail(email: String): Boolean

    fun existsByLogin(login: String): Boolean
}