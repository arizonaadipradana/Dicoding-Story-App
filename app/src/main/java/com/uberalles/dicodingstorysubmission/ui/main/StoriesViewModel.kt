package com.uberalles.dicodingstorysubmission.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uberalles.dicodingstorysubmission.repos.StoriesRepository
import com.uberalles.dicodingstorysubmission.repos.Story

class StoriesViewModel(repository: StoriesRepository) : ViewModel() {
    val story: LiveData<PagingData<Story>> = repository.getStories().cachedIn(viewModelScope)
}
