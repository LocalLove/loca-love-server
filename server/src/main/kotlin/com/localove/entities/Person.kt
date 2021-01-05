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
    var login: String,

    @Column(name = "name")
    var name: String,

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(name = "birth_date")
    var birthDate: LocalDate,

    @Column(name = "status")
    var status: String = "",

    @Column(name = "bio")
    var bio: String = "",

    @OneToOne
    @JoinColumn(name = "avatar_id")
    var avatar: Photo? = null

) : Identifiable() {

    enum class Gender {
        MALE,
        FEMALE
    }

    @OneToMany(mappedBy = "owner")
    @OrderBy("last_update_time DESC")
    val photos: MutableList<Photo> = mutableListOf()

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

}

@Repository
interface PersonRepository: JpaRepository<Person, Long>