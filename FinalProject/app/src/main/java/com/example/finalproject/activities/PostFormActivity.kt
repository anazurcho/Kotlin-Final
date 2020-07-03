package com.example.finalproject.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.post_create_form.*
import android.Manifest
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.util.*

class PostFormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_create_form)

        createPost.setOnClickListener {
            postCreateFun(it)
        }
    }

    private fun postCreateFun(view: View) {
        val titlePost = postTitle.text.toString()
        val infoPost = postInfo.text.toString()

        val database = FirebaseDatabase.getInstance().reference
        val postMap: Map<String, String> =
            mapOf(
                "titlePost" to titlePost,
                "infoPost" to infoPost
            )
        database.child("posts").push().setValue(postMap)

        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }

}