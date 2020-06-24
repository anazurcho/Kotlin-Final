package com.example.finalproject.fragments


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.activities.CompanyFormActivity
import com.example.finalproject.adapters.CompanyAdapter
import com.example.finalproject.dto.Company
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_companies.*

class CompaniesFragment : Fragment(R.layout.activity_companies) {

    private val listOfCompanies: ArrayList<Company> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyRecycler.layoutManager = LinearLayoutManager(context)


        createCompany.setOnClickListener {
            startActivity(Intent(context, CompanyFormActivity::class.java))
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
                    val adapter =
                        CompanyAdapter(
                            listOfCompanies
                        )
                    companyRecycler.adapter = adapter
                }
            }

        })
    }

}