package com.uberalles.dicodingstorysubmission.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field: SerializedName("description")
    val description: String
)
