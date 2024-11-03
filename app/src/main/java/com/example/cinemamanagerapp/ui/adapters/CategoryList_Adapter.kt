package com.example.cinemamanagerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.CategoryResponse

class CategoryList_Adapter(
    private var categoryList: MutableList<CategoryResponse>,
    private val onCategoryClick: (Int) -> Unit // Callback cho sự kiện click
) : RecyclerView.Adapter<CategoryList_Adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoryName: TextView = itemView.findViewById(R.id.tv_CategoryName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category, parent, false) // Đảm bảo layout category.xml tồn tại
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCategoryName.text = categoryList[position].name
        holder.itemView.setOnClickListener {
            onCategoryClick(categoryList[position].category_id) // Gọi callback với ID thể loại
        }
    }

    fun updateCategories(categories: List<CategoryResponse>) {
        categoryList.clear()
        categoryList.addAll(categories)
        notifyDataSetChanged()
    }
}
