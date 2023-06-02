package com.uberalles.dicodingstorysubmission.ui.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.uberalles.dicodingstorysubmission.api.ApiConfig
import com.uberalles.dicodingstorysubmission.response.SignInResponse
import com.uberalles.dicodingstorysubmission.response.SignUpResponse
import com.uberalles.dicodingstorysubmission.utils.UserPrefs
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val preferences: UserPrefs) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse> = _signUpResponse

    private val loginResponse = MutableLiveData<SignInResponse>()

    fun signUp(token: Context, name: String, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).postSignUp(name, email, password)
        client.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _signUpResponse.value = response.body()
                } else {
                    Toast.makeText(token, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("AuthViewModel", "onFailure")
            }

        })
    }

    fun signIn(token: Context, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).postLogin(email, password)
        client.enqueue(object : Callback<SignInResponse> {
            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {
                _isLoading.value = true
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    loginResponse.value = response.body()

                    val token = response.body()!!.loginResult.token
                    val userId = response.body()!!.loginResult.userId
                    val name = response.body()!!.loginResult.name

                    viewModelScope.launch {
                        preferences.saveLoginSession(token, userId, name)
                        Log.d(
                            "AuthViewModel",
                            "\nToken= $token,\nName= $name,\nuserId= $userId"
                        )
                    }
                } else {
                    Log.e("AuthViewModel", "onFailure: ${response.message()}")
                    _isLoading.value = false
                    Toast.makeText(token, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("AuthViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserPreferences(property: String): LiveData<String> {
        return when (property) {
            "userId" -> preferences.getUserId().asLiveData()
            "token" -> preferences.getToken().asLiveData()
            "name" -> preferences.getName().asLiveData()
            else -> preferences.getUserId().asLiveData()
        }.also { liveData ->
            liveData.observeForever {
                Log.e("UserPreferences", "Retrieved user preference: $property =$it")
            }
        }
    }

    fun clearUserPreferences() {
        viewModelScope.launch {
            preferences.clearLoginSession()
        }
    }

}