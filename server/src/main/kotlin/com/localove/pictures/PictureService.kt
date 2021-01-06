package com.localove.pictures

import com.localove.entities.Picture
import com.localove.entities.PictureRepository
import com.localove.exceptions.NotFoundException
import com.localove.exceptions.UnsupportedTypeException
import com.localove.profile.PersonService
import org.hibernate.annotations.NotFound
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
            throw UnsupportedTypeException(
                "Wrong type: $type. Should be one of ${properties.supportedTypes}"
            )
        }
        pictureRepository.save(
            Picture(
                owner = personService.getCurrentPerson(),
                lastUpdateTime = LocalDateTime.now(),
                type = type,
                bytes = bytes
            )
        )
    }

    fun getPicture(pictureId: Long): Picture {
        return pictureRepository
            .findById(pictureId)
            .orElseThrow {
                NotFoundException("Picture with such id not found: $pictureId")
            }
    }

    private fun isCorrectType(type: String): Boolean {
        return properties.supportedTypes.contains(type)
    }
}