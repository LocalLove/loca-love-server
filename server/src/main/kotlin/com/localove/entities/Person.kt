package com.localove.entities

import com.localove.Identifiable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
class Person(

    @Column(name = "login")
    val login: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(name = "birth_date")
    val birthDate: LocalDate,

    @Column(name = "status")
    val status: String,

    @Column(name = "bio")
    val bio: String,

    @ManyToOne
    @JoinColumn(name = "avatar_id")
    val avatar: Photo

) : Identifiable() {

    enum class Gender {
        MALE,
        FEMALE
    }

    @ManyToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "users_likes",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "liked_user_id")]
    )
    val likedPersons: MutableSet<Person> = hashSetOf()

    @ManyToMany(mappedBy = "likedPersons")
    val likedByPersons: MutableSet<Person> = hashSetOf()

}

@Repository
interface PersonRepository: JpaRepository<Person, Long>