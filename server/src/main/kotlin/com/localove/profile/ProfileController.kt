package com.localove.profile

import com.localove.api.ErrorType
import com.localove.api.user.Profile
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
}

import com.localove.api.edit.PasswordDto
import com.localove.api.security.TokenDto
import com.localove.security.jwt.JwtAuthentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/user")
class ProfileController(
) {
    @PostMapping("/check-password")
    fun checkPassword(passwordDto: PasswordDto): TokenDto =
}

fun JwtAuthentication.toDto(): TokenDto = TokenDto(token)