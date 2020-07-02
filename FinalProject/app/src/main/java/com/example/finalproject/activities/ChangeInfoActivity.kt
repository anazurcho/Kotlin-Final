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
import com.example.finalproject.dto.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.company_create_form.*
import kotlinx.android.synthetic.main.company_item.view.*
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
        imageURLBtn.setOnClickListener {
            chooseImageClicked(it)
        }
        saveBtn.setOnClickListener {

            val name = editTextPersonName.text.toString()
            val phone = editTextPhone.text.toString()
            val details = editTextDetails.text.toString()
            val age = editTextAge.text.toString()

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
                        val userInfoMap: Map<String, String> =
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
            }

//            val userInfo = UserInfo(name, phone, age, details)

//            db.child(auth.currentUser?.uid!!).setValue(userInfo)

        }

    }

    private fun init() {

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("UserInfo")

        this.userInfoChangeListener()

    }

    private fun userInfoChangeListener() {

        db.child(auth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(data: DataSnapshot) {

                val userInfo = data.getValue(UserInfo::class.java) ?: return

                phoneTextView.text = userInfo.phone
                nameTextView.text = userInfo.name
                detailsTextView.text = userInfo.details
                ageTextView.text = userInfo.age
            }

        })

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

        val selectedImage = data!!.data

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