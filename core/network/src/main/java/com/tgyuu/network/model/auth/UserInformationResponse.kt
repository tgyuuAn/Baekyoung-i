package com.tgyuu.network.model.auth

data class UserInformationResponse(
    val userId: String = "",
    val nickName: String = "",
    val gender: String = "",
    val major: String = "",
    val grade: Int = -1,
    val fcmToken: String = "",
    val registrationDate: String = "",
)
