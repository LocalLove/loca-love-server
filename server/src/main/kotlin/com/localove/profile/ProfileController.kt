package com.localove.profile

import com.localove.api.ErrorType
import com.localove.api.edit.NewPasswordDto
import com.localove.api.edit.PasswordDto
import com.localove.api.user.Profile
import com.localove.entities.Person
import com.localove.exceptions.WrongPasswordException
import com.localove.exceptions.WrongTokenException
import com.localove.security.UserService
import com.localove.util.Response
import com.localove.util.Validations
import com.localove.util.isValid
import io.konform.validation.Validation
import javassist.NotFoundException
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

    @PostMapping("/check-password")
    fun checkPassword(@RequestBody passwordDto: PasswordDto): ResponseEntity<*> {
        return try {
            Response.ok(userService.checkPassword(passwordDto.password))
        } catch (exc: WrongPasswordException) {
            Response.error(ErrorType.NOT_FOUND, exc.localizedMessage)
        }
    }

    @PostMapping("/edit-password")
    fun editPassword(@RequestBody newPasswordDto: NewPasswordDto): ResponseEntity<*> {
        return if (passwordValidation.isValid(newPasswordDto)) {
            return try {
                userService.editPassword(newPasswordDto.password, newPasswordDto.token)
                Response.ok()
            } catch (exc: WrongTokenException) {
                Response.error(ErrorType.WRONG_TOKEN, exc.localizedMessage)
            }
        } else {
            Response.error(ErrorType.VALIDATION_ERROR, "Invalid password")
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

