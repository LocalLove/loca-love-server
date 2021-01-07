package com.localove.profile

import com.localove.api.edit.BaseProfileEditDto
import com.localove.pictures.PictureService
import com.localove.security.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.reflect.KMutableProperty0

@Service
class PersonEditService(
    private val personService: PersonService,
    private val pictureService: PictureService,
    private val userService: UserService
) {
    @Transactional
    fun firstStartConfiguration(
        avatarBytes: ByteArray,
        avatarType: String,
        userInfo: BaseProfileEditDto
    ) {
        pictureService.addPicture(avatarBytes, avatarType)
        editProfile(userInfo)
        userService.firstStartConfiguration()
    }

    @Transactional
    fun editProfile(editProfileDto: BaseProfileEditDto) {
        val person = personService.getCurrentPerson()

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