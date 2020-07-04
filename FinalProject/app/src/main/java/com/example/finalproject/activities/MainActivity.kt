package com.example.finalproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.R
import com.example.finalproject.adapters.ViewPagerFragmentAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewPagerFragmentAdapter: ViewPagerFragmentAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPagerFragmentAdapter = ViewPagerFragmentAdapter(this)

        viewpager.adapter = viewPagerFragmentAdapter

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = "Object ${(position + 1)}"
            when (position) {
                0 -> {
                    tab.text = "Posts"
                    tab.setIcon(R.drawable.ic_remove_red_eye_black_24dp)
                }
                1 -> {
                    tab.text = "My Posts"
                    tab.setIcon(R.drawable.ic_baseline_account_circle_24)
                }
            }
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        } else if (item.itemId == R.id.settings) {
            if (auth.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


}


