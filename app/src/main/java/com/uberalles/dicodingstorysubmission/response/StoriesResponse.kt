package com.uberalles.dicodingstorysubmission.response

import com.google.gson.annotations.SerializedName
import com.uberalles.dicodingstorysubmission.repos.Story

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story>?,
    @SerializedName("message")
    val message: String
)