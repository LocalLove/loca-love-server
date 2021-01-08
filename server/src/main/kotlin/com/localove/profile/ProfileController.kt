package com.localove.profile

import com.localove.api.ErrorType
import com.localove.api.user.Profile
import com.localove.api.user.ProfileCard
import com.localove.entities.Person
import com.localove.exceptions.InvalidUserException
import com.localove.exceptions.NotFoundException
import com.localove.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/user")
class ProfileController(
    private val personService: PersonService
) {
    @GetMapping("/{userId}")
    fun getProfile(@PathVariable userId: Long): ResponseEntity<*> {
        return try {
            Response.ok(personService.getPerson(userId).toProfile())
        } catch (exc: NotFoundException) {
            Response.error(ErrorType.NOT_FOUND, "User with such id not found")
        }
    }

    @GetMapping("/{userId}/card")
    fun getProfileCard(@PathVariable userId: Long): ResponseEntity<*> {
        return try {
            Response.ok(personService.getPerson(userId).toProfileCard())
        } catch (exc: NotFoundException) {
            Response.error(ErrorType.NOT_FOUND, "User with such id not found")
        }
    }

    @PostMapping("/{userId}/like")
    fun likeUser(@PathVariable userId: Long): ResponseEntity<*> {
        return try {
            personService.likeUser(userId)
            Response.ok()
        } catch (exc: NotFoundException) {
            Response.error(ErrorType.NOT_FOUND, "User with such id not found")
        } catch (exc: InvalidUserException) {
            Response.error(ErrorType.INVALID_USER, exc.localizedMessage)
        }
    }

    @GetMapping("/profile")
    fun getProfile(): ResponseEntity<*> {
        return try{
            Response.ok(personService.getCurrentPerson())
        } catch (exc: IllegalArgumentException){
            Response.error(ErrorType.ACCESS_DENIED, exc.localizedMessage)
        }
    }

    fun Person.toProfile() = Profile(
        id = id!!,
        age = birthDate.until(LocalDate.now()).years,
        login = login,
        name = name,
        gender = gender,
        status = status,
        isLiked = personService.isLikedByCurrentPerson(this),
        bio = bio,
        pictureIds = pictures.map { it.id!! }
    )

    fun Person.toProfileCard() = ProfileCard(
        id = id!!,
        age = birthDate.until(LocalDate.now()).years,
        name = name,
        gender = gender,
        status = status,
        avatarId = avatar?.id!!
    )
}
