package com.uberalles.dicodingstorysubmission.ui.googlemaps

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uberalles.dicodingstorysubmission.api.ApiConfig
import com.uberalles.dicodingstorysubmission.repos.Story
import com.uberalles.dicodingstorysubmission.response.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {
    private val _mapStoryListAll = MutableLiveData<ArrayList<Story>>()
    val storyListAll: MutableLiveData<ArrayList<Story>> = _mapStoryListAll

    fun getMapStoryListAll(token: Context){
        val client = ApiConfig.getApiService(token).getLocation(1)
        client.enqueue(object : Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null){
                    _mapStoryListAll.value = response.body()?.listStory as ArrayList<Story>?
                } else {
                    Log.e("MapViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e("MapViewModel", "onFailure: ${t.message}")
            }
        })
    }

}