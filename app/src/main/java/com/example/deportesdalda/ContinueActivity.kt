package com.example.deportesdalda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.deportesdalda.databinding.ActivityContinueBinding

class ContinueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContinueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContinueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Deportes Dalda")
    }

    fun cont(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}