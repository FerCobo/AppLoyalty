package com.example.deportesdalda

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.example.deportesdalda.databinding.ActivityShopBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class ShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Catálogo de productos")

        table()
    }

    private fun table(){
        db.collection("products").document("1").get().addOnSuccessListener {document ->
            val name = document.getString("name")
            val description = document.getString("description")
            val price = document.getString("price")
            binding.feature1.text = "$name\n\n$description\n\nPrecio: $price"
        }
        val image = FirebaseStorage.getInstance().reference.child("productos/1.jpg")
        image.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.image1)
        }

        db.collection("products").document("2").get().addOnSuccessListener {document ->
            val name = document.getString("name")
            val description = document.getString("description")
            val price = document.getString("price")
            binding.feature2.text = "$name\n\n$description\n\nPrecio: $price"
        }
        val image2 = FirebaseStorage.getInstance().reference.child("productos/2.jpg")
        image2.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.image2)
        }

        db.collection("products").document("3").get().addOnSuccessListener {document ->
            val name = document.getString("name")
            val description = document.getString("description")
            val price = document.getString("price")
            binding.feature3.text = "$name\n\n$description\n\nPrecio: $price"
        }
        val image3 = FirebaseStorage.getInstance().reference.child("productos/3.jpg")
        image3.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.image3)
        }

        db.collection("products").document("4").get().addOnSuccessListener {document ->
            val name = document.getString("name")
            val description = document.getString("description")
            val price = document.getString("price")
            binding.feature4.text = "$name\n\n$description\n\nPrecio: $price"
        }
        val image4 = FirebaseStorage.getInstance().reference.child("productos/4.jpg")
        image4.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.image4)
        }

        db.collection("products").document("5").get().addOnSuccessListener {document ->
            val name = document.getString("name")
            val description = document.getString("description")
            val price = document.getString("price")
            binding.feature5.text = "$name\n\n$description\n\nPrecio: $price"
        }
        val image5 = FirebaseStorage.getInstance().reference.child("productos/5.jpg")
        image5.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.image5)
        }
    }

    private lateinit var timer: Timer
    private val noDelay = 0L
    private val everyFiveSeconds = 28000L

    override fun onResume() {
        super.onResume()

        val timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    randomImages()
                }
            }
        }

        timer = Timer()
        timer.schedule(timerTask, noDelay, everyFiveSeconds)
    }

    override fun onPause() {
        super.onPause()

        timer.cancel()
        timer.purge()
    }

    private fun randomImages(){
        binding.advert2.visibility = View.GONE
        binding.advert3.visibility = View.GONE
        binding.advert4.visibility = View.GONE
        val image = FirebaseStorage.getInstance().reference.child("anuncios/Image1.jpg")
        val localFile = File.createTempFile("tempImage", ".jpg")
        image.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.advert.setImageBitmap(bitmap)
        }

        Handler().postDelayed(Runnable {
            binding.advert.visibility = View.GONE
            binding.advert2.visibility = View.VISIBLE
            val image = FirebaseStorage.getInstance().reference.child("anuncios/Image2.jpg")
            val localFile = File.createTempFile("tempImage", ".jpg")
            image.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.advert2.setImageBitmap(bitmap)
                Handler().postDelayed(Runnable {
                    binding.advert2.visibility = View.GONE
                    binding.advert3.visibility = View.VISIBLE
                    val image = FirebaseStorage.getInstance().reference.child("anuncios/Image3.jpg")
                    val localFile = File.createTempFile("tempImage", ".jpg")
                    image.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.advert3.setImageBitmap(bitmap)
                        Handler().postDelayed(Runnable {
                            binding.advert3.visibility = View.GONE
                            binding.advert4.visibility = View.VISIBLE
                            val image = FirebaseStorage.getInstance().reference.child("anuncios/Image4.jpg")
                            val localFile = File.createTempFile("tempImage", ".jpg")
                            image.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                binding.advert4.setImageBitmap(bitmap)
                            }
                        }, 7000)
                    }
                }, 7000)
            }
        }, 7000)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_back -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}