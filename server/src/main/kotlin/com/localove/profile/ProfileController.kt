package com.localove.profile

import com.localove.api.ErrorType
import com.localove.api.edit.NewPasswordDto
import com.localove.api.edit.PasswordDto
import com.localove.api.security.TokenDto
import com.localove.api.user.Profile
import com.localove.api.user.ProfileCard
import com.localove.entities.Person
import com.localove.exceptions.InvalidTokenException
import com.localove.exceptions.InvalidUserException
import com.localove.exceptions.NotFoundException
import com.localove.exceptions.WrongPasswordException
import com.localove.security.UserService
import com.localove.util.Response
import com.localove.util.Validations
import com.localove.util.isValid
import io.konform.validation.Validation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/user")
class ProfileController(
    private val personService: PersonService,
    private val userService: UserService
) {
    private val passwordValidation = Validation<NewPasswordDto> {
        NewPasswordDto::password {
            run(Validations.passwordValidation)
        }
    }

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

    @PostMapping("/check-password")
    fun checkPassword(@RequestBody passwordDto: PasswordDto): ResponseEntity<*> {
        return try {
            Response.ok(TokenDto(userService.checkPassword(passwordDto.password)))
        } catch (exc: WrongPasswordException) {
            Response.error(ErrorType.NOT_FOUND, exc.localizedMessage)
        }
    }

    @PostMapping("/edit/password")
    fun editPassword(@RequestBody newPasswordDto: NewPasswordDto): ResponseEntity<*> {
        if (!passwordValidation.isValid(newPasswordDto)) {
            Response.error(ErrorType.VALIDATION_ERROR, "Invalid password")
        }

        return try {
            userService.editPassword(newPasswordDto.password, newPasswordDto.token)
            Response.ok()
        } catch (exc: InvalidTokenException) {
            Response.error(ErrorType.INVALID_TOKEN, exc.localizedMessage)
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

