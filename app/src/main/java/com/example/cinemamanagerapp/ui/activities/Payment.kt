package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinemamanagerapp.R

import com.example.cinemamanagerapp.ui.adapters.Food_Adapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.api.FoodDrinksResponse
import com.example.cinemamanagerapp.api.RetrofitClient

class Payment : AppCompatActivity() {
    private lateinit var lv_FoodList: ListView
    private lateinit var adapter: Food_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lv_FoodList = findViewById(R.id.lv_FoodList)
        adapter = Food_Adapter(emptyList())
        lv_FoodList.adapter = adapter

        fetchFoodData()
    }

    private fun fetchFoodData() {
        lifecycleScope.launch {
            try {
                RetrofitClient.apiService.getAllFoodDrink()
                    .enqueue(object : Callback<List<FoodDrinksResponse>> {
                        override fun onResponse(
                            call: Call<List<FoodDrinksResponse>>,
                            response: Response<List<FoodDrinksResponse>>
                        ) {
                            if (response.isSuccessful) {
                                val foodDrinksList = response.body() ?: emptyList()

                                val foodList = foodDrinksList.map { foodDrinksResponse ->
                                    FoodDrinksResponse(
                                        food_drink_id = foodDrinksResponse.food_drink_id,
                                        name = foodDrinksResponse.name,
                                        type = foodDrinksResponse.type, // Đảm bảo giá trị này được truyền vào
                                        price = foodDrinksResponse.price,
                                        image = foodDrinksResponse.image ?: "",
                                        quantity = 1 // Hoặc bất kỳ giá trị mặc định nào bạn muốn
                                    )
                                }

                                adapter = Food_Adapter(foodList)
                                lv_FoodList.adapter = adapter
                            } else {
                                Log.e("Payment", "Lấy dữ liệu món ăn không thành công: ${response.code()} ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<List<FoodDrinksResponse>>, t: Throwable) {
                            Log.e("Payment", "Lỗi khi lấy dữ liệu món ăn: ${t.message}", t)
                        }
                    })
            } catch (e: Exception) {
                Log.e("Payment", "Ngoại lệ khi lấy dữ liệu món ăn: ${e.message}", e)
            }
        }
    }




}
