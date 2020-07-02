package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.finalproject.R
import com.example.finalproject.dto.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_settings_user.*


class SettingsActivity : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_user)
        this.init()
        Log.d("MyData", auth.currentUser?.uid!!)
        this.buttons()
    }
    private fun init() {
        auth = FirebaseAuth.getInstance()
        currentUser.text = auth.currentUser?.email
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
                nameTextView.text = userInfo.name
                ageTextView.text = userInfo.age
                descriptionTextView.text = userInfo.details
                userPhoneTextView.text = userInfo.phone
                Picasso.get().load(userInfo.imageURL).into(imageURL)
            }
        })
    }

    private fun buttons(){
        signOutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        changePasswordBtn.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        changeInfoBtn.setOnClickListener {
            startActivity(Intent(this, ChangeInfoActivity::class.java))
        }
        goBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}