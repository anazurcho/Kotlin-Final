package com.example.finalproject.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.company_create_form.*
import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class CompanyFormActivity : AppCompatActivity() {
    private val imageId = UUID.randomUUID().toString() + ".jpg"
    private val companyId = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_create_form)

        createCompany.setOnClickListener {
            companyCreateFun(it)
        }
        companyLogo.setOnClickListener {
            chooseImageClicked(it)
        }
    }

    private fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    private fun companyCreateFun(view: View) {
        val titleCompany = companyTitle.text.toString()
        val infoCompany = companyInfo.text.toString()

        companyLogo.isDrawingCacheEnabled = true
        companyLogo.buildDrawingCache()
        val bitmap = (companyLogo.drawable as BitmapDrawable).bitmap
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
                    val database = FirebaseDatabase.getInstance().reference
                    val companyMap: Map<String, String> =
                        mapOf(
                            "titleCompany" to titleCompany,
                            "imageURL" to downloadUrl,
                            "infoCompany" to infoCompany
                        )
                    database.child("companies").child(companyId).setValue(companyMap)
                }

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

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
                companyLogo.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }


}