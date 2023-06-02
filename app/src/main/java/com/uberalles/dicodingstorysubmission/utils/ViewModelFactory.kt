package com.uberalles.dicodingstorysubmission.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uberalles.dicodingstorysubmission.ui.auth.AuthViewModel

//class ViewModelFactory private constructor(private val repository: AppRepository) :
//    ViewModelProvider.NewInstanceFactory() {
class ViewModelFactory(private val prefs: UserPrefs) : ViewModelProvider.NewInstanceFactory() {

//    companion object {
//        @Volatile
//        private var instance: ViewModelFactory? = null
//
//        fun getInstance(context: Context): ViewModelFactory {
//            return instance ?: synchronized(this) {
//                instance ?: ViewModelFactory(context)
//            }.also { instance = it }
//        }
//
//        object Injection {
//            fun provideRepository(context: Context): StoriesRepository {
//                val apiService = ApiConfig.getApiService(context)
//                return StoriesRepository(apiService)
//            }
//        }
//    }

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