package com.example.finalproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.activities.PostFormActivity
import com.example.finalproject.adapters.PostAdapter
import com.example.finalproject.dto.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_posts.*

class PostsFragment : Fragment(R.layout.activity_posts) {
    private lateinit var adapter: PostAdapter
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        initView()

        createPost.setOnClickListener {
            startActivity(Intent(context, PostFormActivity::class.java))
        }

        if (auth.currentUser == null) {
            createPost.visibility = View.GONE
        }

        val database = FirebaseDatabase.getInstance().getReference("posts")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var posts: ArrayList<Post> = ArrayList()
                    for (postInstance in snapshot.children.reversed()) {
                        val post = postInstance.getValue(Post::class.java)
                        if (post != null) {
                            posts.add(post)
                        }
                    }
                    adapter.updatePosts(posts)
                }
            }

        })
    }

    private fun initView() {
        adapter = PostAdapter(ArrayList<Post>())
        postRecycler.layoutManager = LinearLayoutManager(context)
        postRecycler.adapter = adapter
    }
}