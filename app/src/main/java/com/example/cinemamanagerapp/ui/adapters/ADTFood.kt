package com.example.cinemamanagerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.model.Food

class ADTFood(private var food : List<Food>?) : BaseAdapter() {
    override fun getCount(): Int {
        return 6;
        //if(food == null) 0 else food.size
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(p2?.context).inflate(R.layout.food,null)
        return view;
    }
}