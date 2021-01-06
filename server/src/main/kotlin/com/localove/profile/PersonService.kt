package com.localove.profile

import com.localove.entities.Person
import com.localove.entities.PersonRepository
import com.localove.exceptions.NotFoundException
import com.localove.exceptions.InvalidUserException
import com.localove.security.AuthorizedUserInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    fun isLikedByCurrentPerson(person: Person): Boolean {
        val currentPerson = getCurrentPerson()
        return currentPerson.likedPersons.contains(person)
    }

    @Transactional
    fun likeUser(userId: Long) {
        val currentPerson = getCurrentPerson()
        val otherPerson = getPerson(userId)

        if (currentPerson == otherPerson) {
            throw InvalidUserException("Specified user is the same as the currently authorized one")
        }

        if (currentPerson.likedPersons.contains(otherPerson)) {
            currentPerson.likedPersons.remove(otherPerson)
        } else {
            currentPerson.likedPersons.add(otherPerson)
        }
    }

}