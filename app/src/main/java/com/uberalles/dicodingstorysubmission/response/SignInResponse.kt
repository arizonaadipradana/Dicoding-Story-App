package com.uberalles.dicodingstorysubmission.response

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("loginResult")
    val loginResult: SignInResult,

    @SerializedName("message")
    val message: String
)