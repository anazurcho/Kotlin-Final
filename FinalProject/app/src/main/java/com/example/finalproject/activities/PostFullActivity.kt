package com.example.finalproject.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.adapters.CommentAdapter
import com.example.finalproject.dto.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.post_full_activity.*
import kotlin.properties.Delegates

class PostFullActivity() : AppCompatActivity()  {
    private var postTitle by Delegates.notNull<String>()
    private var idPost by Delegates.notNull<String>()
    private var postInfo by Delegates.notNull<String>()

    private lateinit var adapter: CommentAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_full_activity)
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null) {
            createComment.visibility = View.GONE
        }
        initView()
        this.init()

        goBack.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        createComment.setOnClickListener {
            val intent = Intent(this, CommentFormActivity::class.java)
            intent.putExtra("idPost", idPost)
            startActivity(intent)
            finish()
        }
    }

    private fun init(){
        this.getPostInfo()
        this.getComments()
    }

    private fun getPostInfo(){
        postTitle = intent.extras?.getString("postTitle")!!
        idPost = intent.extras?.getString("idPost")!!
        postInfo = intent.extras?.getString("postInfo")!!
        titlePost.text = postTitle
        infoPost.text = postInfo
    }

    private fun initView() {
        adapter = CommentAdapter(ArrayList<Comment>())
        commentRecycler.layoutManager = LinearLayoutManager(this)
        commentRecycler.adapter = adapter
    }

    private fun getComments(){
        val database = FirebaseDatabase.getInstance().getReference("comments")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var comments: ArrayList<Comment> = ArrayList()
                    for (commentInstance in snapshot.children.reversed()) {
                        val comment = commentInstance.getValue(Comment::class.java)
                        if (comment != null && comment.idPost == idPost) {
                            comments.add(comment)
                        }
                    }
                    postCommentsSize.text = "Comments (${comments.size.toString()})"
                    adapter.updateComments(comments)
                }
            }

        })
    }

}
