package com.example.finalproject.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.activities.PostFullActivity
import com.example.finalproject.dto.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostAdapter(private var posts: ArrayList<Post> )
    : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: Post) {
            itemView.titlePost.text = post.titlePost
            itemView.infoPost.text = post.infoPost
            itemView.setOnClickListener{
                val context = itemView.context
                val intent = Intent(context, PostFullActivity::class.java)
                intent.putExtra("idPost", post.idPost)
                intent.putExtra("postTitle", post.titlePost)
                intent.putExtra("postInfo", post.infoPost)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(
            v
        )
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    fun updatePosts(posts: ArrayList<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }
}