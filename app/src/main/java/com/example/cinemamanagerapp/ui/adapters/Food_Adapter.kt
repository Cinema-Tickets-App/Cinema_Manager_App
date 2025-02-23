package com.example.cinemamanagerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.FoodDrinksResponse
import com.example.cinemamanagerapp.ui.activities.Payment

class Food_Adapter(private var foodList: MutableList<FoodDrinksResponse>?) : BaseAdapter() {

    override fun getCount(): Int {
        return foodList?.size ?: 0
    }

    override fun getItem(position: Int): FoodDrinksResponse {
        return foodList?.get(position) ?: FoodDrinksResponse(
            food_drink_id = 0,
            name = "Unknown",
            type = "Unknown",
            price = 0.0,
            image = "",
            quantity = 0
        )
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
        val priceTextView = view.findViewById<TextView>(R.id.tv_FoodPrice)
        val addButton = view.findViewById<ImageButton>(R.id.IMB_Add)
        val subButton = view.findViewById<ImageButton>(R.id.IMB_Sub)

        nameTextView.text = food.name
        amountTextView.text = food.quantity.toString()
        priceTextView.text = "${food.price.toInt()}đ" // Chuyển food.price thành Int để hiển thị

        Glide.with(view.context).load(food.image).into(imageView)

        // Nút cộng
        addButton.setOnClickListener {
            food.quantity++ // Tăng số lượng
            amountTextView.text = food.quantity.toString() // Cập nhật số lượng trên giao diện
            (parent?.context as? Payment)?.updateSelectedFoodPrice() // Cập nhật tổng tiền
        }

        // Nút trừ
        subButton.setOnClickListener {
            if (food.quantity > 0) { // Đảm bảo số lượng không giảm dưới 0
                food.quantity-- // Giảm số lượng
                amountTextView.text = food.quantity.toString() // Cập nhật số lượng trên giao diện
                (parent?.context as? Payment)?.updateSelectedFoodPrice() // Cập nhật tổng tiền
            }
        }

        return view
    }
}
