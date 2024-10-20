package com.example.cinemamanagerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.model.Category

class ADTCategoryList(private var categoryList: MutableList<Category>) :
    RecyclerView.Adapter<ADTCategoryList.ViewHold>() {

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tv_CategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        holder.tvCategoryName.text = categoryList[position].name // Giả sử `name` là thuộc tính của `Category`
    }

    fun updateCategories(categories: List<Category>) {
        categoryList.clear()
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }
}
