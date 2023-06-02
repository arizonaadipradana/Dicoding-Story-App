package com.uberalles.dicodingstorysubmission.api

import com.uberalles.dicodingstorysubmission.response.SignInResponse
import com.uberalles.dicodingstorysubmission.response.SignUpResponse
import com.uberalles.dicodingstorysubmission.response.StoriesResponse
import com.uberalles.dicodingstorysubmission.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("register")
    @FormUrlEncoded
    fun postSignUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignInResponse>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<UploadResponse>
//
//    @GET("stories")
//    suspend fun getStoriesWithLocation(
//        @Query("location") location: Int,
//    ) : StoriesResponse
//

}