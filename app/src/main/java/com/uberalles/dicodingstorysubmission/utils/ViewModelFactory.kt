package com.uberalles.dicodingstorysubmission.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uberalles.dicodingstorysubmission.ui.auth.AuthViewModel

//class ViewModelFactory private constructor(private val repository: AppRepository) :
//    ViewModelProvider.NewInstanceFactory() {
class ViewModelFactory(private val prefs: UserPrefs) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(prefs) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}