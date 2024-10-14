package com.example.cinemamanagerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R

class ADTCategoryList(private val categoryList : Array<String>?) : RecyclerView.Adapter<ADTCategoryList.ViewHold>() {
    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return 5;
        //if(categoryList == null) 0 else categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
         var tv = holder.itemView.findViewById<TextView>(R.id.tv_CategoryName);
    }
}