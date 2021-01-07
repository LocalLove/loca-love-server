package com.localove.profile

import com.localove.api.ErrorType
import com.localove.api.edit.BaseProfileEditDto
import com.localove.api.edit.EmailDto
import com.localove.api.edit.NewPasswordDto
import com.localove.api.edit.PasswordDto
import com.localove.api.security.TokenDto
import com.localove.exceptions.AlreadyExistsException
import com.localove.exceptions.InvalidTokenException
import com.localove.exceptions.NotExistEmailException
import com.localove.exceptions.UnsupportedTypeException
import com.localove.exceptions.WrongPasswordException
import com.localove.security.UserService
import com.localove.util.Response
import com.localove.util.Validations
import com.localove.util.throwIfNotValid
import io.konform.validation.Validation
import javassist.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class ProfileEditController(
    private val userService: UserService,
    private val personEditService: PersonEditService
) {
    private val passwordValidation = Validation<NewPasswordDto> {
        NewPasswordDto::password {
            run(Validations.passwordValidation)
        }
    }

    private val emailValidation = Validation<EmailDto> {
        EmailDto::email {
            run(Validations.emailValidation)
        }
    }

    @PostMapping("/user/edit/password")
    fun editPassword(@RequestBody newPasswordDto: NewPasswordDto): ResponseEntity<*> {
        passwordValidation.throwIfNotValid(newPasswordDto)

        return try {
            userService.editPassword(newPasswordDto.password, newPasswordDto.token)
            Response.ok()
        } catch (exc: InvalidTokenException) {
            Response.error(ErrorType.INVALID_TOKEN, exc.localizedMessage)
        }
    }

    @PostMapping("/user/check-password")
    fun checkPassword(@RequestBody passwordDto: PasswordDto): ResponseEntity<*> {
        return try {
            Response.ok(TokenDto(userService.checkPassword(passwordDto.password)))
        } catch (exc: WrongPasswordException) {
            Response.error(ErrorType.NOT_FOUND, exc.localizedMessage)
        }
    }

    @PostMapping("/confirm-new")
    fun confirmNewEmail(
        @RequestParam("token") token: String
    ): ResponseEntity<*> {
        return try {
            userService.confirmNewEmail(token)
            Response.ok()
        } catch (exc: InvalidTokenException) {
            Response.error(ErrorType.INVALID_TOKEN, exc.localizedMessage)
        }
    }

    @PutMapping("/user/edit")
    fun baseEditProfile(@RequestBody editProfileDto: BaseProfileEditDto): ResponseEntity<*> {
        return try {
            personEditService.editProfile(editProfileDto)
            Response.ok()
        } catch (exc: NotFoundException) {
            Response.error(ErrorType.NOT_FOUND, "User with such id not found")
        }
    }

    @PostMapping("/user/first-start")
    fun firstStartConfiguration(
        @RequestPart("avatar")
        avatar: MultipartFile,
        @RequestPart("userInfo")
        userInfo: BaseProfileEditDto
    ): ResponseEntity<*> {
        avatar.contentType ?: return Response.error(
            ErrorType.VALIDATION_ERROR,
            "Content type should be defined"
        )

        return try {
            personEditService.firstStartConfiguration(
                avatar.bytes,
                avatar.contentType!!,
                userInfo
            )
            Response.ok()
        } catch (exc: UnsupportedTypeException) {
            Response.error(ErrorType.VALIDATION_ERROR, exc.localizedMessage)
        }
    }

    @PostMapping("/user/edit/email")
    fun editEmail(@RequestBody emailDto: EmailDto): ResponseEntity<*> {
        emailValidation.throwIfNotValid(emailDto)

        return try {
            userService.editEmail(emailDto.email)
            Response.ok()
        } catch (exc: AlreadyExistsException) {
            Response.error(ErrorType.EMAIL_EXIST, exc.localizedMessage)
        }
    }

    @PostMapping("password-restore")
    fun passwordRestoreGetEmail(@RequestParam email: String): ResponseEntity<*>{
        return try{
            userService.restorePassword(email)
            Response.ok()
        } catch (exc: NotExistEmailException){
            Response.error(ErrorType.EMAIL_NOT_EXIST, exc.localizedMessage)
        }
    }
}
