package com.localove.security.role

import com.localove.Identifiable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Roles")
class Role(
    val name: Name
) : Identifiable() {
    enum class Name {
        USER,
        UNCONFIRMED
    }
}

interface RoleRepository : JpaRepository<Role, Int> {
    fun findByName(name: Role.Name): Optional<Role>
}