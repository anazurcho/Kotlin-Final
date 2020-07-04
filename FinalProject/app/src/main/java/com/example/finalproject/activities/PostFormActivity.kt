package com.example.finalproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import kotlinx.android.synthetic.main.post_create_form.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PostFormActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_create_form)

        createPost.setOnClickListener {
            postCreateFun(it)
        }
        auth = FirebaseAuth.getInstance()
    }


    private fun postCreateFun(view: View) {
        val titlePost = postTitle.text.toString()
        val infoPost = postInfo.text.toString()

        val database = FirebaseDatabase.getInstance().reference
        val postMap: Map<String, String> =
            mapOf(
                "titlePost" to titlePost,
                "infoPost" to infoPost,
                "uid" to auth.currentUser!!.uid
            )
        database.child("posts").push().setValue(postMap)

        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}