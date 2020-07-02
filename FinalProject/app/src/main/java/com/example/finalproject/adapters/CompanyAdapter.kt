package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.dto.Company
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.company_item.view.*

class CompanyAdapter(private var companies: ArrayList<Company> )
    : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    class CompanyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(company: Company) {
            itemView.titleCompany.text = company.titleCompany
            itemView.infoCompany.text = company.infoCompany
            Picasso.get().load(company.imageURL).into(itemView.imageURL)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.company_item, parent, false)
        return CompanyViewHolder(
            v
        )
    }

    override fun getItemCount(): Int = companies.size

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        holder.bind(companies[position])
    }

}