package com.uberalles.dicodingstorysubmission.utils

sealed class ResultOutput<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultOutput<T>()
    data class Error(val error: String) : ResultOutput<Nothing>()
    object Loading : ResultOutput<Nothing>()
}