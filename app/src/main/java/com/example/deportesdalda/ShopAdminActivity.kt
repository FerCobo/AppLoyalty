package com.example.deportesdalda

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.deportesdalda.databinding.ActivityShopAdminBinding
import com.example.deportesdalda.databinding.ActivityShopBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ShopAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopAdminBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var ImageUri: Uri
    private val imageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyToolbar().show(this, "Actualizar catálogo")

        table()

        binding.selectImage.setOnClickListener(){
            selectImage()
        }

        binding.uploadProduct.setOnClickListener(){
            uploadProduct()
        }
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

    private fun uploadProduct(){
        val nameImage = binding.idTxt.text.toString()
        val filename = "$nameImage.jpg"
        val storage = FirebaseStorage.getInstance().getReference("productos/$filename")

        if (!TextUtils.isEmpty(binding.idTxt.text.toString()) && !TextUtils.isEmpty(binding.nameTxt.text.toString())
            && !TextUtils.isEmpty(binding.descriptionTxt.text.toString()) && !TextUtils.isEmpty(binding.priceTxt.text.toString())) {

                storage.putFile(ImageUri).addOnCompleteListener{
                    binding.firebaseImage.setImageURI(null)
                    Toast.makeText(this, "Producto subido", Toast.LENGTH_LONG).show()
                }

                db.collection("products")
                    .document(binding.idTxt.text.toString())
                    .set(
                        hashMapOf(
                            "name" to binding.nameTxt.text.toString(),
                            "description" to binding.descriptionTxt.text.toString(),
                            "price" to binding.priceTxt.text.toString()
                        )
                    )
        } else {
            Toast.makeText(this, "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun table(){
        db.collection("products").document("1").get().addOnSuccessListener {document ->
            val name = document.getString("name")
            val description = document.getString("description")
            val price = document.getString("price")
            binding.feature1.text = "$name\n\n$description\n\nPrecio: $price€"
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
            binding.feature2.text = "$name\n\n$description\n\nPrecio: $price€"
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
            binding.feature3.text = "$name\n\n$description\n\nPrecio: $price€"
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
            binding.feature4.text = "$name\n\n$description\n\nPrecio: $price€"
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
            binding.feature5.text = "$name\n\n$description\n\nPrecio: $price€"
        }
        val image5 = FirebaseStorage.getInstance().reference.child("productos/5.jpg")
        image5.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()
            Glide.with(this)
                .load(imageURL)
                .into(binding.image5)
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
                startActivity(Intent(this, ShopAdminActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}