package com.localove.profile

import com.localove.entities.Person
import com.localove.entities.PersonRepository
import com.localove.exceptions.NotFoundException
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

    fun getCurrentPerson(): Person {
        return personRepository.findCurrentUser()
            ?: throw IllegalArgumentException("Not authorized")
    }

    fun isLikedByCurrentUser(person: Person): Boolean {
        return getCurrentPerson()
            .likedPersons
            .contains(person)
    }
}