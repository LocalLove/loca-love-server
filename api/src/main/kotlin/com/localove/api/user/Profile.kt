package com.localove.api.user

enum class Gender {
    MALE,
    FEMALE
}

data class Profile (
    val id: Long,
    val age: Int,
    val login: String,
    val name: String,
    val gender: Gender,
    val status: String?,
    val bio: String?,
    val isLiked: Boolean,
    val pictureIds: Set<Long>
)