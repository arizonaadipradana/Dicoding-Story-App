package com.uberalles.dicodingstorysubmission.api

import com.uberalles.dicodingstorysubmission.utils.UserPrefs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var token: UserPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { token.getToken().first() }
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}