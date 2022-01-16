package com.example.deportesdalda

import android.app.Application

class DeportesDaldaApplication: Application() {

    companion object{
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}