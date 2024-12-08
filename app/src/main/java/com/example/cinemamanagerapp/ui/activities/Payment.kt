package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.*
import com.example.cinemamanagerapp.ui.adapters.Food_Adapter
import com.example.cinemamanagerapp.zalopay.Api.CreateOrder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class Payment : AppCompatActivity() {

    // UI Elements
    private lateinit var tvSelectedSeats: TextView
    private lateinit var tvPaymentSum: TextView
    private lateinit var lvFoodList: ListView

    // Variables
    private lateinit var adapter: Food_Adapter
    private var selectedSeats: String = ""
    private var totalAmount: Int = 0
    private var foodList: MutableList<FoodDrinksResponse> = mutableListOf()
    private var showtimeId: Int = -1
    private var roomName: String = ""
    private var discountPercentage: Int = 0 // Mặc định không có giảm giá

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ZaloPay SDK
        ZaloPaySDK.init(2554, Environment.SANDBOX)
        setContentView(R.layout.activity_payment)

        // Receive data from previous activity
        receiveIntentData()

        // Initialize UI elements
        initUI()

        // Fetch food data
        fetchFoodData()

        // Setup discount code functionality
        setupDiscountCode()

        // Setup payment button listener
        findViewById<Button>(R.id.btn_payment).setOnClickListener {
            payment(totalAmount)
        }
    }

    private fun receiveIntentData() {
        selectedSeats = intent.getStringExtra("SELECTED_SEATS") ?: ""
        totalAmount = intent.getIntExtra("TOTAL_AMOUNT", 0)
        showtimeId = intent.getIntExtra("SHOWTIME_ID", -1)
        if (showtimeId == -1) {
            Toast.makeText(this, "Lỗi: Suất chiếu không hợp lệ", Toast.LENGTH_SHORT).show()
            finish()
        }

        roomName = intent.getStringExtra("ROOM_NAME") ?: "Không xác định"
        Log.d("Payment", "Room Name nhận được: $roomName")
    }

    private fun initUI() {
        tvSelectedSeats = findViewById(R.id.tv_selectedSeats)
        tvPaymentSum = findViewById(R.id.tv_paymentSum)
        lvFoodList = findViewById(R.id.lv_FoodList)

        tvSelectedSeats.text = "Ghế đã chọn: $selectedSeats"
        tvPaymentSum.text = "Tổng thanh toán: $totalAmount đ"
    }

    private fun setupDiscountCode() {
        val etDiscountCode = findViewById<EditText>(R.id.et_discount_code)
        val btnApplyDiscount = findViewById<Button>(R.id.btn_apply_discount)

        btnApplyDiscount.setOnClickListener {
            val discountCode = etDiscountCode.text.toString().trim()
            if (discountCode.isNotEmpty()) {
                applyDiscountCode(discountCode)
            } else {
                Toast.makeText(this, "Vui lòng nhập mã giảm giá!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fetch food data from server
    private fun fetchFoodData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getAllFoodDrink().execute()
                if (response.isSuccessful) {
                    val foodDrinksList = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        foodList.clear()
                        foodList.addAll(foodDrinksList)
                        adapter = Food_Adapter(foodList)
                        lvFoodList.adapter = adapter
                        updateSelectedFoodPrice()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@Payment,
                            "Lỗi khi lấy dữ liệu món ăn",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("Payment", "Failed to fetch food data: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Payment,
                        "Lỗi khi lấy dữ liệu món ăn: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("Payment", "Error fetching food data: ${e.message}", e)
                }
            }
        }
    }

    // Update total price when food quantity changes
    internal fun updateSelectedFoodPrice() {
        var selectedFoodPrice = 0
        for (food in foodList) {
            if (food.quantity > 0) {
                selectedFoodPrice += (food.price * food.quantity).toInt()
            }
        }
        updateTotalPrice(selectedFoodPrice)
    }

    private fun applyDiscountCode(discountCode: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getPromotionByCode(discountCode).execute()
                if (response.isSuccessful) {
                    val promotion = response.body()
                    if (promotion != null) {
                        discountPercentage = promotion.discount_percentage
                        val discountedAmount = totalAmount - (totalAmount * discountPercentage / 100)
                        withContext(Dispatchers.Main) {
                            totalAmount = discountedAmount
                            tvPaymentSum.text = "Tổng thanh toán (sau giảm): ${totalAmount}đ"
                            Toast.makeText(
                                this@Payment,
                                "Áp dụng mã giảm giá thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@Payment,
                                "Mã giảm giá không hợp lệ!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@Payment,
                            "Lỗi khi kiểm tra mã giảm giá!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@Payment,
                        "Lỗi kết nối: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateTotalPrice(selectedFoodPrice: Int) {
        // Lấy lại giá trị ban đầu của tổng tiền
        totalAmount = intent.getIntExtra("TOTAL_AMOUNT", 0)
        totalAmount += selectedFoodPrice
        tvPaymentSum.text = "Tổng thanh toán: ${totalAmount}đ"
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("Payment", "onNewIntent: $intent")
        ZaloPaySDK.getInstance().onResult(intent)
    }

    private fun payment(totalAmount: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("Payment", "Initiating payment with total amount: $totalAmount")
                val order = CreateOrder()
                val data = order.createOrder(totalAmount.toString())
                val code = data.getString("returncode")
                val token = data.getString("zptranstoken")

                Log.d("Payment", "Received return code: $code and token: $token")

                if (code == "1" && !token.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Log.d("Payment", "Starting ZaloPay payment with token: $token")
                        ZaloPaySDK.getInstance().payOrder(
                            this@Payment,
                            token,
                            "demozpdk://app",
                            object : PayOrderListener {
                                override fun onPaymentSucceeded(p0: String?, p1: String?, p2: String?) {
                                    Log.d("Payment", "Payment succeeded.")
                                    handlePaymentSuccess()
                                }

                                override fun onPaymentCanceled(p0: String?, p1: String?) {
                                    Log.d("Payment", "Payment canceled.")
                                    Toast.makeText(
                                        this@Payment,
                                        "Thanh toán bị hủy",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onPaymentError(p0: ZaloPayError?, p1: String?, p2: String?) {
                                    Log.e("Payment", "Payment error: $p0, $p1, $p2")
                                    Toast.makeText(
                                        this@Payment,
                                        "Lỗi thanh toán",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e(
                            "Payment",
                            "Invalid return code or missing token: code=$code, token=$token"
                        )
                        Toast.makeText(this@Payment, "Lỗi mã trả về: $code", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Payment", "Lỗi khi tạo đơn hàng: ${e.message}", e)
                    Toast.makeText(
                        this@Payment,
                        "Lỗi khi tạo đơn hàng: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun handlePaymentSuccess() {
        lifecycleScope.launch(Dispatchers.IO) {
            // Lấy userId từ SharedPreferences
            val sharedPreferences =
                getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("user_id", -1)
            Log.d("Payment", "User ID nhận được: $userId")

            // Kiểm tra nếu userId hợp lệ
            if (userId == -1) {
                withContext(Dispatchers.Main) {
                    Log.e("Payment", "User ID không hợp lệ")
                    Toast.makeText(
                        this@Payment,
                        "Lỗi lấy thông tin người dùng",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            val seats = selectedSeats.split(",")
            val foodDrinks = foodList.filter { it.quantity > 0 }
                .map { FoodDrink(it.food_drink_id, it.quantity) }

            val ticketRequest = TicketRequest(
                user_id = userId,
                showtime_id = showtimeId,
                seats = seats,
                food_drinks = foodDrinks,
                payment_method = "ZaloPay",
                price = totalAmount,
                room_name = roomName // Đảm bảo trường này được bao gồm
            )

            // Log chi tiết dữ liệu gửi lên server
            Log.d("Payment", "Dữ liệu vé gửi lên server: $ticketRequest")

            // Gửi yêu cầu lên server để đặt vé
            sendTicketData(ticketRequest)
        }
    }

    private fun sendTicketData(ticketRequest: TicketRequest) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Log dữ liệu JSON gửi lên server
                val gson = Gson()
                val ticketJson = gson.toJson(ticketRequest)
                Log.d("Payment", "JSON gửi lên server: $ticketJson")

                val response = RetrofitClient.apiService.bookTicket(ticketRequest).execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val ticketResponse = response.body()
                        if (ticketResponse != null) {
                            Log.d("Payment", "Đặt vé thành công: ${ticketResponse.message}")
                            Log.d("Payment", "Thông tin vé: ${ticketResponse.ticket}")
                            Log.d("Payment", "Ghế đã chọn: ${ticketResponse.seats}")
                            Log.d("Payment", "Món ăn/đồ uống: ${ticketResponse.food_drinks}")
                            Log.d("Payment", "Giá vé: ${ticketResponse.price}")
                            Log.d("Payment", "Room Name from server: ${ticketResponse.room_name}")

                            Toast.makeText(
                                this@Payment,
                                "Đặt vé thành công!",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Chuyển đến màn hình chính sau khi thanh toán thành công
                            val intent = Intent(this@Payment, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            intent.putExtra("payment_status", "Thanh toán thành công")
                            startActivity(intent)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("Payment", "Lỗi khi đặt vé: ${response.message()}")
                        Toast.makeText(this@Payment, "Lỗi khi đặt vé", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Payment", "Lỗi kết nối: ${e.message}")
                    Toast.makeText(this@Payment, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}