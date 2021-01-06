package com.localove.security.entities

import com.localove.Identifiable
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
@Table(name = "Roles")
class Role(

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    val name: Name

) : Identifiable() {

    enum class Name {
        USER,
        UNCONFIRMED,
        NEWBIE
    }

}

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: Role.Name): Role?
}