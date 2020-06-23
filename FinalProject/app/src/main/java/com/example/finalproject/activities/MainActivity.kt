package com.example.finalproject.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.Company
import com.example.finalproject.CompanyAdapter
import com.example.finalproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.createCompany
import kotlinx.android.synthetic.main.activity_settings.*


class MainActivity  : AppCompatActivity(){


    private val auth = FirebaseAuth.getInstance()
    private val listOfCompanies: ArrayList<Company> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        companyRecycler.layoutManager = LinearLayoutManager(this)


        createCompany.setOnClickListener {
            startActivity(Intent(this, CompanyFormActivity::class.java))
        }


        val database = FirebaseDatabase.getInstance().getReference("companies")
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (companyInstance in snapshot.children.reversed()) {
                        val company = companyInstance.getValue(Company::class.java)
                        if (company != null) {
                            listOfCompanies.add(company)
                        }
                    }
                    val adapter = CompanyAdapter(listOfCompanies)
                    companyRecycler.adapter = adapter
                }
            }

        })
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
            finish()
        } else if (item.itemId == R.id.settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


}


