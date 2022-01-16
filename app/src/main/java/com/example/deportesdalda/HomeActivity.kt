package com.example.deportesdalda

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.deportesdalda.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*


class HomeActivity : AppCompatActivity() {

    class MyClass{
        companion object{
            var activity: Activity? = null
        }
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyClass.activity = this@HomeActivity

        MyToolbar().show(this, "Deportes Dalda")

        val bundle = intent.extras
        val emailEditText = bundle?.getString("email")
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(emailEditText.toString()).get().addOnSuccessListener {
            binding.textName.setText(it.get("nombre") as String?)
            binding.textPoints.setText(it.get("puntos") as String?)
        }
    }

    fun QR(view: View){
        val email = DeportesDaldaApplication.prefs.getEmail()
        val writer = QRCodeWriter()
        try {
            val biMatrix = writer.encode(email, BarcodeFormat.QR_CODE, 512, 512)
            val width = biMatrix.width
            val height = biMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width){
                for(y in 0 until height){
                    bmp.setPixel(x, y, if(biMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            binding.ivQRCode?.setImageBitmap(bmp)
        }catch (e: WriterException){
            e.printStackTrace()
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
        binding.advert2?.visibility = View.GONE
        binding.advert3?.visibility = View.GONE
        binding.advert4?.visibility = View.GONE
        val image = FirebaseStorage.getInstance().reference.child("anuncios/Image1.JPG")
        val localFile = File.createTempFile("tempImage", ".jpg")
        image.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.advert?.setImageBitmap(bitmap)
        }

        Handler().postDelayed(Runnable {
            binding.advert?.visibility = View.GONE
            binding.advert2?.visibility = View.VISIBLE
            val image = FirebaseStorage.getInstance().reference.child("anuncios/Image2.JPG")
            val localFile = File.createTempFile("tempImage", ".jpg")
            image.getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.advert2?.setImageBitmap(bitmap)
                Handler().postDelayed(Runnable {
                    binding.advert2?.visibility = View.GONE
                    binding.advert3?.visibility = View.VISIBLE
                    val image = FirebaseStorage.getInstance().reference.child("anuncios/Image3.JPG")
                    val localFile = File.createTempFile("tempImage", ".jpg")
                    image.getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.advert3?.setImageBitmap(bitmap)
                        Handler().postDelayed(Runnable {
                            binding.advert3?.visibility = View.GONE
                            binding.advert4?.visibility = View.VISIBLE
                            val image = FirebaseStorage.getInstance().reference.child("anuncios/Image4.JPG")
                            val localFile = File.createTempFile("tempImage", ".jpg")
                            image.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                binding.advert4?.setImageBitmap(bitmap)
                            }
                        }, 7000)
                    }
                }, 7000)
            }
        }, 7000)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_info -> {
                startActivity(Intent(this, InfoActivity::class.java))
                true
            }
            R.id.nav_edit -> {
                startActivity(Intent(this, EditActivity::class.java))
                true
            }
            R.id.nav_refresh -> {
                val bundle = intent.extras
                val emailEditText = bundle?.getString("email")
                db = FirebaseFirestore.getInstance()
                db.collection("users").document(emailEditText.toString()).get().addOnSuccessListener {
                    binding.textName.setText(it.get("nombre") as String?)
                    binding.textPoints.setText(it.get("puntos") as String?)
                }
                true
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            R.id.nav_shop -> {
                startActivity(Intent(this, ShopActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
