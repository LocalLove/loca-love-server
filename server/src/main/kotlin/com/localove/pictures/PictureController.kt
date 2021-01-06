package com.localove.pictures

import com.localove.api.ErrorType
import com.localove.exceptions.UnsupportedTypeException
import com.localove.util.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/pictures")
class PictureController(
    private val pictureService: PictureService,
) {
    @PostMapping
    fun addPicture(
        @RequestPart("picture")
        picture: MultipartFile
    ): ResponseEntity<*> {
        picture.contentType ?: return Response.error(
            ErrorType.VALIDATION_ERROR,
            "Content type should be defined"
        )

        return try {
            pictureService.addPicture(picture.bytes, picture.contentType!!)
            Response.ok()
        } catch (exc: UnsupportedTypeException) {
            Response.error(ErrorType.VALIDATION_ERROR, exc.localizedMessage)
        }
    }
}