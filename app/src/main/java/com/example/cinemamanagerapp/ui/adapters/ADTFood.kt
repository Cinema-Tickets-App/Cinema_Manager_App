package com.example.cinemamanagerapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.model.Food

class ADTFood(private var foodList: List<Food>?) : BaseAdapter() {
    override fun getCount(): Int {
        return foodList?.size ?: 0
    }

    override fun getItem(position: Int): Food {
        return foodList?.get(position) ?: Food(0, "Unknown", 0.0, "")
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.food, parent, false)

        val food = getItem(position)

        val imageView = view.findViewById<ImageView>(R.id.img_FoodImage)
        val nameTextView = view.findViewById<TextView>(R.id.tv_FoodName)
        val amountTextView = view.findViewById<TextView>(R.id.tv_FoodAmount)
        val addButton = view.findViewById<ImageButton>(R.id.IMB_Add)
        val subButton = view.findViewById<ImageButton>(R.id.IMB_Sub)


        nameTextView.text = food.name
        amountTextView.text = food.quantity.toString()


        Glide.with(view.context).load(food.image).into(imageView)


        addButton.setOnClickListener {
            food.quantity++
            amountTextView.text = food.quantity.toString()
        }


        subButton.setOnClickListener {
            if (food.quantity > 0) {
                food.quantity--
                amountTextView.text = food.quantity.toString()
            }
        }

        return view
    }
}

