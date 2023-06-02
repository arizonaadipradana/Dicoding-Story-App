package com.uberalles.dicodingstorysubmission.utils

import android.content.Context
import android.content.SharedPreferences

object Preferences {

    fun initPref(context: Context, name: String): SharedPreferences {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun editorPreference(context: Context, name: String): SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }

    fun saveToken(token: String, context: Context) {
        val editor = editorPreference(context, "onSignIn")
        editor.putString("token", token)
        editor.apply()
    }

    //fun get userid from shared preferences
    fun getUserId(context: Context): String? {
        val sharedPref = context.getSharedPreferences("onSignIn", Context.MODE_PRIVATE)
        return sharedPref.getString("userId", null)
    }

//    fun getToken(context: Context): String? {
//        val sharedPref = context.getSharedPreferences("onSignIn", Context.MODE_PRIVATE)
//        return sharedPref.getString("token", null)
//    }

    fun logOut(context: Context) {
        val editor = editorPreference(context, "onSignIn")
        editor.remove("token")
        editor.remove("userId")
        editor.remove("name")
        editor.apply()
    }

}