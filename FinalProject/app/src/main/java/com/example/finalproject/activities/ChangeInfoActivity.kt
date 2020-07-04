package com.example.finalproject.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.goBack

import java.io.ByteArrayOutputStream
import java.util.*

class ChangeInfoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private val imageId = UUID.randomUUID().toString() + ".jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        this.init()
        this.buttons()

        val name = this.intent.extras?.getString("name")
        val age = this.intent.extras?.getString("age")
        val bio = this.intent.extras?.getString("description")
        val phone = this.intent.extras?.getString("phone")
        val imgUrl = this.intent.extras?.getString("imgUrl")

        editTextPersonName.setText(name)
        editTextAge.setText(age)
        editTextPhone.setText(phone)
        editTextDetails.setText(bio)
        Picasso.get().load(imgUrl).into(imageURLBtn)
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("UserInfo")

    }
    private fun buttons(){
        imageURLBtn.setOnClickListener {
            chooseImageClicked(it)
        }
        goBack.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        saveBtn.setOnClickListener {

            val name = editTextPersonName.text.toString()
            val phone = editTextPhone.text.toString()
            val details = editTextDetails.text.toString()
            val age = editTextAge.text.toString().toInt()

            imageURLBtn.isDrawingCacheEnabled = true
            imageURLBtn.buildDrawingCache()
            val bitmap = (imageURLBtn.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val upload = FirebaseStorage.getInstance().reference
                .child("images").child(imageId).putBytes(data)

            upload.addOnFailureListener {
                Toast.makeText(this, "Upload has failed!", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                var downloadUrl: String

                FirebaseStorage.getInstance()
                    .reference.child("images/$imageId").downloadUrl.addOnCompleteListener {
                        downloadUrl = it.result.toString()
                        val userInfoMap: Map<String, Any> =
                            mapOf(
                                "name" to name,
                                "imageURL" to downloadUrl,
                                "phone" to phone,
                                "details" to details,
                                "age" to age
                            )
                        db.child(auth.currentUser?.uid!!).setValue(userInfoMap)
                    }
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SettingsActivity::class.java))
                finish()
            }
        }

    }

    private fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    private fun chooseImageClicked(view: View) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data?.data

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                imageURLBtn.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }
}