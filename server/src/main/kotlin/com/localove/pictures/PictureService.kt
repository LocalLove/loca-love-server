package com.localove.pictures

import com.localove.entities.Picture
import com.localove.entities.PictureRepository
import com.localove.exceptions.UnsupportedTypeException
import com.localove.profile.PersonService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PictureService(
    private val pictureRepository: PictureRepository,
    private val personService: PersonService,
    private val properties: PicturesProperties
) {

    fun addPicture(bytes: ByteArray, type: String) {
        if (!isCorrectType(type)) {
            throw UnsupportedTypeException("Wrong type: $type.\n Should be ${properties.supportedTypes}")
        }
        pictureRepository.save(
            Picture(
                personService.getCurrentPerson(),
                LocalDateTime.now(),
                type,
                bytes
            )
        )
    }

    private fun isCorrectType(type: String): Boolean {
        return properties.supportedTypes.contains(type)
    }
}