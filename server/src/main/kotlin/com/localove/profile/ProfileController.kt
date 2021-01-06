package com.localove.profile

import com.localove.api.ErrorType
import com.localove.api.user.Profile
import com.localove.api.user.ProfileCard
import com.localove.entities.Person
import com.localove.util.Response
import javassist.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    fun getProfileCard(@PathVariable userId: Long): ResponseEntity<*>{
        return try{
            Response.ok(personService.getPerson(userId).toProfileCard())
        }catch (exc: NotFoundException){
            Response.error(ErrorType.NOT_FOUND, "User with such id not found")
        }
    }

    fun Person.toProfile() = Profile(
        id = id!!,
        age = birthDate.until(LocalDate.now()).years,
        login = login,
        name = name,
        gender = gender,
        status = status,
        isLiked = personService.isLikedByCurrentUser(this),
        bio = bio,
        pictureIds = photos.map { it.id!! }
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
