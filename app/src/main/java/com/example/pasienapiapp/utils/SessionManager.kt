package com.example.pasienapiapp.utils

import android.content.Context

class SessionManager(context: Context) {

    private val pref = context.getSharedPreferences("session_pref", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        pref.edit().putString("token", token).apply()
    }

    fun saveUserName(name: String) {
        pref.edit().putString("user_name", name).apply()
    }

    fun getToken(): String? = pref.getString("token", null)

    fun getUserName(): String? = pref.getString("user_name", null)

    fun clear() {
        pref.edit().clear().apply()
    }
}