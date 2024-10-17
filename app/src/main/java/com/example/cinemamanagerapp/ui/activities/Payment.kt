package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.model.Food
import com.example.cinemamanagerapp.ui.adapters.ADTFood
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.api.RetrofitClient

class Payment : AppCompatActivity() {
    private lateinit var lv_FoodList: ListView
    private lateinit var adapter: ADTFood

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
        adapter = ADTFood(emptyList())
        lv_FoodList.adapter = adapter

        fetchFoodData()
    }

    private fun fetchFoodData() {
        lifecycleScope.launch {
            try {
                Log.d("Payment", "Đang lấy dữ liệu món ăn từ API")
                RetrofitClient.apiService.getFoodDrinks().enqueue(object : Callback<List<Food>> {
                    override fun onResponse(
                        call: Call<List<Food>>,
                        response: Response<List<Food>>
                    ) {
                        if (response.isSuccessful) {
                            val foodList = response.body() ?: emptyList()
                            Log.d("Payment", "Đã nhận được ${foodList.size} món ăn")
                            adapter = ADTFood(foodList)
                            lv_FoodList.adapter = adapter
                        } else {

                            Log.e(
                                "Payment",
                                "Lấy dữ liệu món ăn không thành công: ${response.code()} ${response.message()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<List<Food>>, t: Throwable) {

                        Log.e("Payment", "Lỗi khi lấy dữ liệu món ăn: ${t.message}", t)
                    }
                })
            } catch (e: Exception) {

                Log.e("Payment", "Ngoại lệ khi lấy dữ liệu món ăn: ${e.message}", e)
            }
        }
    }

}
