package com.localove.profile

import com.localove.api.edit.BaseProfileEditDto
import com.localove.entities.Person
import com.localove.entities.PersonRepository
import com.localove.exceptions.InvalidUserException
import com.localove.exceptions.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KMutableProperty0

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

    fun isLikedByCurrentPerson(person: Person): Boolean {
        return getCurrentPerson()
            .likedPersons
            .contains(person)
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
            return
        }

        currentPerson.likedPersons.add(otherPerson)
    }

    fun editProfile(editProfileDto: BaseProfileEditDto) {
        val person = getCurrentPerson()

        updateIfNonNull(person::login, editProfileDto.login)
        updateIfNonNull(person::name, editProfileDto.name)
        updateIfNonNull(person::birthDate, editProfileDto.dateOfBirth)
        updateIfNonNull(person::status, editProfileDto.status)
        updateIfNonNull(person::bio, editProfileDto.bio)
    }

    private fun <T> updateIfNonNull(propertyToUpdate: KMutableProperty0<T>, newValue: T?) =
        newValue?.let {
            propertyToUpdate.setter.call(newValue)
        }

}