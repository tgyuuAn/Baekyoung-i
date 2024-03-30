package com.pknu.network.model.auth

data class UserResponse(
    val userId: String = "",
    val nickName: String = "",
    val sex: String = "",
    val major: String = "",
    val grade: Int = -1,
)