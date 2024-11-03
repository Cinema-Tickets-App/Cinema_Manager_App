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


class Food_Adapter(private var foodList: List<FoodDrinksResponse>?) : BaseAdapter() {
    override fun getCount(): Int {
        return foodList?.size ?: 0
    }

    override fun getItem(position: Int): FoodDrinksResponse {
        return foodList?.get(position) ?: FoodDrinksResponse(
            food_drink_id = 0,
            name = "Unknown",
            type = "Unknown",  // Cần thêm thuộc tính này
            price = 0.0,
            image = "",  // Giá trị mặc định cho image
            quantity = 0 // Đảm bảo có giá trị cho quantity
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
        val priceTextView = view.findViewById<TextView>(R.id.tv_FoodPrice) // Thêm TextView cho giá
        val addButton = view.findViewById<ImageButton>(R.id.IMB_Add)
        val subButton = view.findViewById<ImageButton>(R.id.IMB_Sub)

        nameTextView.text = food.name
        amountTextView.text = food.quantity.toString()
        priceTextView.text = food.price.toString() // Chuyển đổi giá sang chuỗi

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

