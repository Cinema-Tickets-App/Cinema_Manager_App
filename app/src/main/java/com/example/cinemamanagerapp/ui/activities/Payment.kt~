package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.FoodDrinksResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.ui.adapters.Food_Adapter
import com.example.cinemamanagerapp.zalopay.Api.CreateOrder
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener


class Payment : AppCompatActivity() {
    private lateinit var lv_FoodList: ListView
    private lateinit var adapter: Food_Adapter
    private lateinit var layoutZalo: LinearLayout
    private lateinit var cb_zalo: CheckBox
    private lateinit var btnPayment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ZaloPaySDK.init(2554, Environment.SANDBOX)

        layoutZalo = findViewById(R.id.layoutZalo)
        cb_zalo = findViewById(R.id.cb_zalo)
        btnPayment = findViewById(R.id.btn_payment)

        lv_FoodList = findViewById(R.id.lv_FoodList)
        adapter = Food_Adapter(emptyList())
        lv_FoodList.adapter = adapter

        fetchFoodData()

        layoutZalo.setOnClickListener {
            cb_zalo.isChecked = !cb_zalo.isChecked
        }

        btnPayment.setOnClickListener {
            if (cb_zalo.isChecked) {
                payment()
            } else {
                Toast.makeText(this, "Vui lòng chọn hình thức thanh toán", Toast.LENGTH_SHORT).show()
            }
        }
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("newIntent", intent.toString())
        ZaloPaySDK.getInstance().onResult(intent)
    }

    private fun payment(){
        val order = CreateOrder()
        try {
            val data = order.createOrder("10000")// gia tien thanh toan
            val code = data.getString("returncode")
            val token = data.getString("zptranstoken")

           if(code.equals("1")){
               ZaloPaySDK.getInstance().payOrder(this, token, "demozpdk://app", object :
                   PayOrderListener {
                   override fun onPaymentSucceeded(p0: String?, p1: String?, p2: String?) {
                       val intent = Intent(this@Payment, MainActivity::class.java)
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                       startActivity(intent)
                   }

                   override fun onPaymentCanceled(p0: String?, p1: String?) {
                       Toast.makeText(this@Payment, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show()
                   }

                   override fun onPaymentError(p0: ZaloPayError?, p1: String?, p2: String?) {
                       Toast.makeText(this@Payment, "Lỗi thanh toan", Toast.LENGTH_SHORT).show()
                   }


               })
           }else{
               Toast.makeText(this, "loi code khac 1", Toast.LENGTH_SHORT).show()
           }
        }catch (e: Exception){
            Log.e("Payment", "Lỗi khi lấy : ${e.printStackTrace()}", e)
        }
    }


}
