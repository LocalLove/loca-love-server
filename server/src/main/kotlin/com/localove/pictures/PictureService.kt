package com.localove.pictures

import com.localove.entities.Picture
import com.localove.entities.PictureRepository
import com.localove.exceptions.AccessDeniedException
import com.localove.exceptions.LastPictureDeletionException
import com.localove.exceptions.NotFoundException
import com.localove.exceptions.UnsupportedTypeException
import com.localove.profile.PersonService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PictureService(
    private val pictureRepository: PictureRepository,
    private val personService: PersonService,
    private val properties: PicturesProperties
) {

    @Transactional
    fun addPicture(bytes: ByteArray, type: String) {
        if (!isCorrectType(type)) {
            throw UnsupportedTypeException(
                "Wrong type: $type. Should be one of ${properties.supportedTypes}"
            )
        }

        val picture = pictureRepository.save(
            Picture(
                owner = personService.getCurrentPerson(),
                lastUpdateTime = LocalDateTime.now(),
                type = type,
                bytes = bytes
            )
        )

        changeProfilePhoto(picture.id!!)
    }

    fun getPicture(pictureId: Long): Picture {
        return pictureRepository
            .findById(pictureId)
            .orElseThrow {
                NotFoundException("Picture with such id not found: $pictureId")
            }
    }

    @Transactional
    fun changeProfilePhoto(pictureId: Long) {
        val picture = getPictureAndCheckOwnership(pictureId)
        picture.lastUpdateTime = LocalDateTime.now()
        picture.owner.avatar = picture
    }

    @Transactional
    fun deletePicture(pictureId: Long) {
        val picture = getPictureAndCheckOwnership(pictureId)
        if (picture.owner.pictures.size <= 1) {
            throw LastPictureDeletionException("Specified photo is the last one owned by current user")
        }

        pictureRepository.delete(picture)
    }

    private fun getPictureAndCheckOwnership(pictureId: Long): Picture {
        val picture = getPicture(pictureId)
        val currentPerson = personService.getCurrentPerson()

        if (picture.owner != currentPerson) {
            throw AccessDeniedException("Current user doesn't own specified picture")
        }

        return picture
    }

    private fun isCorrectType(type: String): Boolean {
        return properties.supportedTypes.contains(type)
    }

}