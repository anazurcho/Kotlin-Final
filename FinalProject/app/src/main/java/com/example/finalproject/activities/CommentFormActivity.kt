package com.example.finalproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.comment_create_form.*
import kotlin.properties.Delegates


class CommentFormActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var idPost by Delegates.notNull<String>()

    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comment_create_form)

        createComment.setOnClickListener {
            commentCreateFun(it)
        }
        auth = FirebaseAuth.getInstance()
        idPost = intent.extras?.getString("idPost")!!

        goBack.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    private fun commentCreateFun(view: View) {
        val comment = comment.text.toString()

        val database = FirebaseDatabase.getInstance().reference
        val postMap: Map<String, String> =
            mapOf(
                "comment" to comment,
                "idPost" to idPost,
                "idUser" to auth.currentUser!!.uid
            )
        database.child("comments").push().setValue(postMap)

        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
//        startActivity(Intent(this, PostFullActivity::class.java))
        finish()
    }

}