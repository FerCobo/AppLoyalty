package com.example.deportesdalda

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.example.deportesdalda.databinding.ActivityAdvertAdminBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AdvertAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdvertAdminBinding
    private lateinit var ImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Subir anuncio")

        listAll()

        binding.selectImage.setOnClickListener(){
            selectImage()
        }

        binding.uploadImage.setOnClickListener(){
            uploadImage()
        }

        binding.cleanTextView.setOnClickListener(){
            binding.firebaseImage.setImageResource(R.color.zxing_transparent)
        }

        binding.delete1TextView.setOnClickListener(){
            val alertDialog = AlertDialog.Builder(this)

            alertDialog.apply {
                setTitle("Eliminar anuncio")
                setMessage("¿Estás seguro/a de que deseas eliminar el anuncio?")
                setPositiveButton("Sí", DialogInterface.OnClickListener({ dialog, id -> yes1() }))
                setNegativeButton("No", null)
            }.create().show()
        }

        binding.delete2TextView.setOnClickListener(){
            val alertDialog = AlertDialog.Builder(this)

            alertDialog.apply {
                setTitle("Eliminar anuncio")
                setMessage("¿Estás seguro/a de que deseas eliminar el anuncio?")
                setPositiveButton("Sí", DialogInterface.OnClickListener({ dialog, id -> yes2() }))
                setNegativeButton("No", null)
            }.create().show()
        }

        binding.delete3TextView.setOnClickListener(){
            val alertDialog = AlertDialog.Builder(this)

            alertDialog.apply {
                setTitle("Eliminar anuncio")
                setMessage("¿Estás seguro/a de que deseas eliminar el anuncio?")
                setPositiveButton("Sí", DialogInterface.OnClickListener({ dialog, id -> yes3() }))
                setNegativeButton("No", null)
            }.create().show()
        }

        binding.delete4TextView.setOnClickListener(){
            val alertDialog = AlertDialog.Builder(this)

            alertDialog.apply {
                setTitle("Eliminar anuncio")
                setMessage("¿Estás seguro/a de que deseas eliminar el anuncio?")
                setPositiveButton("Sí", DialogInterface.OnClickListener({ dialog, id -> yes4() }))
                setNegativeButton("No", null)
            }.create().show()
        }
    }

    private fun yes1(){
        val image1 = FirebaseStorage.getInstance().reference.child("anuncios/Image1.JPG")
        image1.delete()
        Toast.makeText(this, "Anuncio eliminado", Toast.LENGTH_SHORT).show()
    }
    private fun yes2(){
        val image2 = FirebaseStorage.getInstance().reference.child("anuncios/Image2.JPG")
        image2.delete()
        Toast.makeText(this, "Anuncio eliminado", Toast.LENGTH_SHORT).show()
    }
    private fun yes3(){
        val image3 = FirebaseStorage.getInstance().reference.child("anuncios/Image3.JPG")
        image3.delete()
        Toast.makeText(this, "Anuncio eliminado", Toast.LENGTH_SHORT).show()
    }
    private fun yes4(){
        val image4 = FirebaseStorage.getInstance().reference.child("anuncios/Image4.JPG")
        image4.delete()
        Toast.makeText(this, "Anuncio eliminado", Toast.LENGTH_SHORT).show()
    }

    private fun selectImage(){
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            binding.firebaseImage.setImageURI(ImageUri)
        }
    }

    private fun uploadImage(){
        val formatter = SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("anuncios/$filename")

        storage.putFile(ImageUri).addOnCompleteListener{
            binding.firebaseImage.setImageURI(null)
            Toast.makeText(this, "Anuncio subido", Toast.LENGTH_LONG).show()
        }
    }

    private fun listAll(){
        val image1 = FirebaseStorage.getInstance().reference.child("anuncios/Image1.JPG")
        val localFile = File.createTempFile("tempImage", ".jpg")
        image1.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imageAdvert1.setImageBitmap(bitmap)
        }

        val image2 = FirebaseStorage.getInstance().reference.child("anuncios/Image2.JPG")
        val localFile2 = File.createTempFile("tempImage", ".jpg")
        image2.getFile(localFile2).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile2.absolutePath)
            binding.imageAdvert2.setImageBitmap(bitmap)
        }

        val image3 = FirebaseStorage.getInstance().reference.child("anuncios/Image3.JPG")
        val localFile3 = File.createTempFile("tempImage", ".jpg")
        image3.getFile(localFile3).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile3.absolutePath)
            binding.imageAdvert3.setImageBitmap(bitmap)
        }

        val image4 = FirebaseStorage.getInstance().reference.child("anuncios/Image4.JPG")
        val localFile4 = File.createTempFile("tempImage", ".jpg")
        image4.getFile(localFile4).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile4.absolutePath)
            binding.imageAdvert4.setImageBitmap(bitmap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu6, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_back -> {
                finish()
                true
            }
            R.id.nav_refresh -> {
                finish()
                startActivity(Intent(this, AdvertAdminActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}