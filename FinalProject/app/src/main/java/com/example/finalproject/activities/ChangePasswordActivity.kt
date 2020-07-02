package com.example.finalproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_change_password.backBtn
import kotlinx.android.synthetic.main.activity_change_password.editTextTextPassword

class ChangePasswordActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


        saveNewPasswordBtn.setOnClickListener {

            val newPassword = editTextTextPassword.text.toString()

            if (newPassword.isNotEmpty()) {
                auth.currentUser?.updatePassword(newPassword)
                    ?.addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                        }

                    }
            }

        }
        backBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}