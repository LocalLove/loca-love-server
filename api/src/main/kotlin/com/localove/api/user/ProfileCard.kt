package com.localove.api.user

data class ProfileCard(
    val id: Long,
    val age: Int,
    val name: String,
    val gender: Gender,
    val avatarId: Long,
    val status: String?
)