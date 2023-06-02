package com.uberalles.dicodingstorysubmission.response

import com.google.gson.annotations.SerializedName

data class SignInResult(
    @SerializedName("name")
    val name: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("userId")
    val userId: String
)
