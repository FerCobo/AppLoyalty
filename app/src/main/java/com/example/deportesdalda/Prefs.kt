package com.example.deportesdalda

import android.content.Context

class Prefs (val context: Context){
    val SHARED_NAME = "name"
    val SHARED_EMAIL = "email"
    val storage = context.getSharedPreferences(SHARED_NAME, 0)


    fun saveEmail(email:String){
        storage.edit().putString(SHARED_EMAIL, email).apply()
    }

    fun getEmail():String{
        return storage.getString(SHARED_EMAIL, "")!!
    }
}