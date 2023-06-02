package com.uberalles.dicodingstorysubmission.ui.upload

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uberalles.dicodingstorysubmission.api.ApiConfig
import com.uberalles.dicodingstorysubmission.response.UploadResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Callback
import java.io.File

class UploadViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()

    private val _uploadResponse = MutableLiveData<UploadResponse>()
    val uploadResponse: LiveData<UploadResponse> = _uploadResponse

    fun upload(context: Context, image: File, description: String) {
        val storyDescription = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo", image.name, requestImageFile
        )

        _isLoading.value = true

        val client = ApiConfig.getApiService(context).postStory(imageMultipart, storyDescription)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: retrofit2.Call<UploadResponse>,
                response: retrofit2.Response<UploadResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _uploadResponse.value = response.body()
                } else {
                    _error.value = response.message()
                }
            }

            override fun onFailure(call: retrofit2.Call<UploadResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message
            }
        })
    }
}