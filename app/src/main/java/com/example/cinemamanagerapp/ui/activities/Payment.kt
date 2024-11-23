package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.FoodDrinksResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.ui.adapters.Food_Adapter
import com.example.cinemamanagerapp.zalopay.Api.CreateOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class Payment : AppCompatActivity() {

    private lateinit var tvSelectedSeats: TextView
    private lateinit var tvPaymentSum: TextView
    private lateinit var lvFoodList: ListView
    private lateinit var adapter: Food_Adapter

    private var selectedSeats: String = ""
    private var totalAmount: Int = 0 // Tổng tiền đã tính từ màn hình ChooseChair

    private var foodList: MutableList<FoodDrinksResponse> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        ZaloPaySDK.init(2554, Environment.SANDBOX)

        // Nhận dữ liệu từ màn hình trước (ChooseChair)
        selectedSeats = intent.getStringExtra("SELECTED_SEATS") ?: ""
        totalAmount = intent.getIntExtra("TOTAL_AMOUNT", 0) // Nhận tổng tiền từ ChooseChair

        // Ánh xạ các phần tử giao diện
        tvSelectedSeats = findViewById(R.id.tv_selectedSeats)
        tvPaymentSum = findViewById(R.id.tv_paymentSum)
        lvFoodList = findViewById(R.id.lv_FoodList)

        // Hiển thị ghế đã chọn và tổng tiền ban đầu
        tvSelectedSeats.text = "Ghế đã chọn: $selectedSeats"
        tvPaymentSum.text = "Tổng thanh toán: $totalAmount đ" // Hiển thị tổng tiền đã được truyền

        // Lấy dữ liệu món ăn và tính tổng tiền
        fetchFoodData()

        // Khi người dùng nhấn thanh toán
        findViewById<Button>(R.id.btn_payment).setOnClickListener {
            payment(totalAmount) // Truyền totalAmount khi thanh toán
        }
    }

    // Lấy dữ liệu món ăn
    private fun fetchFoodData() {
        lifecycleScope.launch(Dispatchers.IO) { // Sử dụng Dispatchers.IO để chạy trên background thread
            try {
                val response = RetrofitClient.apiService.getAllFoodDrink().execute() // Sử dụng .execute() thay vì enqueue
                if (response.isSuccessful) {
                    val foodDrinksList = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        // Chuyển dữ liệu về main thread để cập nhật UI
                        foodList.clear()
                        foodList.addAll(foodDrinksList)
                        adapter = Food_Adapter(foodList)
                        lvFoodList.adapter = adapter
                        updateSelectedFoodPrice() // Cập nhật giá trị tổng tiền
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Payment, "Lỗi khi lấy dữ liệu món ăn", Toast.LENGTH_SHORT).show()
                        Log.e("Payment", "Failed to fetch food data: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Payment, "Lỗi khi lấy dữ liệu món ăn: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Payment", "Error fetching food data: ${e.message}", e)
                }
            }
        }
    }

    // Tính toán tổng tiền món ăn đã chọn
    internal fun updateSelectedFoodPrice() {
        var selectedFoodPrice = 0
        // Duyệt qua danh sách món ăn và tính tổng tiền của món ăn đã chọn
        for (food in foodList) {
            if (food.quantity > 0) {
                selectedFoodPrice += (food.price * food.quantity).toInt() // Tính tổng tiền món ăn
            }
        }

        // Cập nhật tổng tiền khi có món ăn đã chọn
        updateTotalPrice(selectedFoodPrice)
    }

    // Cập nhật tổng tiền khi có món ăn
    private fun updateTotalPrice(selectedFoodPrice: Int) {
        totalAmount += selectedFoodPrice // Cập nhật totalAmount với giá trị món ăn
        tvPaymentSum.text = "Tổng thanh toán: ${totalAmount}đ" // Cập nhật giao diện với tổng tiền mới
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("newIntent", intent.toString())
        ZaloPaySDK.getInstance().onResult(intent)
    }

    // Xử lý thanh toán
    private fun payment(totalAmount: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("Payment", "Initiating payment with total amount: $totalAmount")
                val order = CreateOrder()
                val data = order.createOrder(totalAmount.toString()) // Sử dụng totalAmount ở đây
                val code = data.getString("returncode")
                val token = data.getString("zptranstoken")

                Log.d("Payment", "Received return code: $code and token: $token")

                // Kiểm tra mã trả về và token
                if (code == "1" && !token.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        // Thanh toán ZaloPay từ Main Thread
                        Log.d("Payment", "Starting ZaloPay payment with token: $token")
                        ZaloPaySDK.getInstance().payOrder(this@Payment, token, "demozpdk://app", object : PayOrderListener {
                            override fun onPaymentSucceeded(p0: String?, p1: String?, p2: String?) {
                                // Thanh toán thành công, chuyển hướng đến MainActivity với thông báo "Thanh toán thành công"
                                Log.d("Payment", "Payment succeeded. Redirecting to MainActivity.")
                                val intent = Intent(this@Payment, MainActivity::class.java)
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)  // Clear all activities above MainActivity
                                intent.putExtra("payment_status", "Thanh toán thành công") // Thêm thông báo vào Intent
                                startActivity(intent)
                            }

                            override fun onPaymentCanceled(p0: String?, p1: String?) {
                                // Thanh toán bị hủy
                                Log.d("Payment", "Payment canceled.")
                                Toast.makeText(this@Payment, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show()
                            }

                            override fun onPaymentError(p0: ZaloPayError?, p1: String?, p2: String?) {
                                // Xử lý lỗi thanh toán
                                Log.e("Payment", "Payment error: ${p0?.let {  }}, $p1, $p2")
                                Toast.makeText(this@Payment, "Lỗi thanh toán", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        // Thông báo lỗi nếu mã trả về không hợp lệ
                        Log.e("Payment", "Invalid return code or missing token: code=$code, token=$token")
                        Toast.makeText(this@Payment, "Lỗi mã trả về: $code", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Log lỗi và thông báo người dùng
                    Log.e("Payment", "Lỗi khi tạo đơn hàng: ${e.message}", e)
                    Toast.makeText(this@Payment, "Lỗi khi tạo đơn hàng: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
