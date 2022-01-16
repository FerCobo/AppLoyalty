package com.example.deportesdalda

import androidx.appcompat.app.AppCompatActivity

class MyToolbar {
    fun show(activities: AppCompatActivity, title:String){
        activities.setSupportActionBar(activities.findViewById(R.id.toolbar))
        activities.supportActionBar?.title=title
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}