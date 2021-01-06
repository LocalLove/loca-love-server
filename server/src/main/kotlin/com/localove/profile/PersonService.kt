package com.localove.profile

import com.localove.entities.Person
import com.localove.entities.PersonRepository
import com.localove.exceptions.NotFoundException
import com.localove.security.AuthorizedUserInfo
import org.springframework.stereotype.Service

@Service
class PersonService(
    private val personRepository: PersonRepository
) {
    fun getPerson(userId: Long): Person {
        return personRepository
            .findById(userId)
            .orElseThrow {
                NotFoundException("Wrong user id: $userId")
            }
    }

    fun getCurrentPerson() = getPerson(AuthorizedUserInfo.getPrincipal().id!!)

    fun isLikedByCurrentUser(person: Person): Boolean {
        val currentPerson = getCurrentPerson()
        return currentPerson.likedPersons.contains(person)
    }
}