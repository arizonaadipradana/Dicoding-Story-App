package com.uberalles.dicodingstorysubmission.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uberalles.dicodingstorysubmission.api.ApiConfig
import com.uberalles.dicodingstorysubmission.repos.StoriesRepository
import com.uberalles.dicodingstorysubmission.repos.Story

class StoriesViewModel(repository: StoriesRepository) : ViewModel() {
    val story: LiveData<PagingData<Story>> = repository.getStories().cachedIn(viewModelScope)
}

class StoriesViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val apiService = ApiConfig.getApiService(context)
            val storyRepository = StoriesRepository(apiService)
            return StoriesViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}