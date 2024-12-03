package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.FoodDrink
import com.example.cinemamanagerapp.api.FoodDrinksResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.TicketRequest
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
    private var showtimeId: Int = -1

    //Giảm giá
    private var discountPercentage: Int = 0 // Mặc định không có giảm giá

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        ZaloPaySDK.init(2554, Environment.SANDBOX)

        // Nhận dữ liệu từ màn hình trước (ChooseChair)
        selectedSeats = intent.getStringExtra("SELECTED_SEATS") ?: ""
        totalAmount = intent.getIntExtra("TOTAL_AMOUNT", 0) // Nhận tổng tiền từ ChooseChair
        showtimeId = intent.getIntExtra("SHOWTIME_ID", -1)
        if (showtimeId == -1) {
            Toast.makeText(this, "Lỗi: Suất chiếu không hợp lệ", Toast.LENGTH_SHORT).show()
            finish()
        }
        // Ánh xạ các phần tử giao diện
        tvSelectedSeats = findViewById(R.id.tv_selectedSeats)
        tvPaymentSum = findViewById(R.id.tv_paymentSum)
        lvFoodList = findViewById(R.id.lv_FoodList)

        // Hiển thị ghế đã chọn và tổng tiền ban đầu
        tvSelectedSeats.text = "Ghế đã chọn: $selectedSeats"
        tvPaymentSum.text = "Tổng thanh toán: $totalAmount đ" // Hiển thị tổng tiền đã được truyền

        // Lấy dữ liệu món ăn và tính tổng tiền
        fetchFoodData()


        //  Giảm giá
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

        // Khi người dùng nhấn thanh toán
        findViewById<Button>(R.id.btn_payment).setOnClickListener {
            payment(totalAmount) // Truyền totalAmount khi thanh toán
        }
    }

    // Lấy dữ liệu món ăn
    private fun fetchFoodData() {
        lifecycleScope.launch(Dispatchers.IO) { // Sử dụng Dispatchers.IO để chạy trên background thread
            try {
                val response = RetrofitClient.apiService.getAllFoodDrink()
                    .execute() // Sử dụng .execute() thay vì enqueue
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

    // Tính toán tổng tiền món ăn đã chọn
    internal fun updateSelectedFoodPrice() {
        var selectedFoodPrice = 0 // Khởi tạo biến lưu tổng tiền mới
        var previousTotal = 0 // Biến lưu tổng tiền cũ trước khi thay đổi số lượng

        // Duyệt qua danh sách món ăn và tính tổng tiền của món ăn đã chọn
        for (food in foodList) {
            if (food.quantity > 0) {
                // Lưu tổng tiền trước khi thay đổi số lượng (để quay lại nếu số lượng giảm)
                previousTotal += (food.price * food.quantity).toInt()
            }
        }

        // Tính toán tổng tiền sau khi thay đổi số lượng
        for (food in foodList) {
            if (food.quantity > 0) {
                selectedFoodPrice += (food.price * food.quantity).toInt() // Cập nhật lại tổng tiền món ăn đã chọn
            }
        }

        // Nếu số lượng giảm, quay lại số tiền trước đó
        if (selectedFoodPrice < previousTotal) {
            selectedFoodPrice = previousTotal // Quay lại giá trị tổng tiền cũ nếu cần
        }

        updateTotalPrice(selectedFoodPrice) // Cập nhật tổng tiền vào giao diện
    }


    //Giảm giá

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



    // Cập nhật tổng tiền thanh toán
    private fun updateTotalPrice(selectedFoodPrice: Int) {
        totalAmount = intent.getIntExtra("TOTAL_AMOUNT", 0) // Lấy lại giá trị ban đầu của tổng tiền từ màn hình trước

        totalAmount += selectedFoodPrice // Cập nhật totalAmount với giá trị món ăn đã chọn
        tvPaymentSum.text = "Tổng thanh toán: ${totalAmount}đ" // Cập nhật giao diện với tổng tiền mới
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("newIntent", intent.toString())
        ZaloPaySDK.getInstance().onResult(intent)
    }

    // Xử lý thanh toán và gửi dữ liệu vé lên server
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
                        ZaloPaySDK.getInstance().payOrder(
                            this@Payment,
                            token,
                            "demozpdk://app",
                            object : PayOrderListener {
                                override fun onPaymentSucceeded(
                                    p0: String?,
                                    p1: String?,
                                    p2: String?
                                ) {
                                    // Thanh toán thành công, gửi dữ liệu vé lên server
                                    Log.d(
                                        "Payment",
                                        "Payment succeeded. Redirecting to MainActivity."
                                    )
                                    //lấy userId từ SharedPreferences
                                    val sharedPreferences =
                                        getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                                    val userId = sharedPreferences.getInt(
                                        "user_id",
                                        -1
                                    ) // Đảm bảo rằng key là "user_id"
                                    Log.d("Payment", "User ID nhận được: $userId")

                                    // Lấy showtimeId từ API hoặc giá trị có sẵn
                                    val showtimeId = intent.getIntExtra("SHOWTIME_ID", -1)

                                    // Kiểm tra nếu userId hợp lệ
                                    if (userId == -1) {
                                        Log.e("Payment", "User ID không hợp lệ")
                                        Toast.makeText(
                                            this@Payment,
                                            "Lỗi lấy thông tin người dùng",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        return
                                    }
                                    val seats =
                                        selectedSeats.split(",") // Ghế đã chọn, chia tách từ chuỗi
                                    val foodDrinks = foodList.filter { it.quantity > 0 }
                                        .map { FoodDrink(it.food_drink_id, it.quantity) }

                                    // Log các thông tin chi tiết về vé
                                    Log.d("Payment", "User ID: $userId")
                                    Log.d("Payment", "Showtime ID: $showtimeId")
                                    Log.d("Payment", "Seats selected: $seats")
                                    Log.d("Payment", "Food and Drinks selected: $foodDrinks")

                                    // Tạo dữ liệu vé
                                    val ticketRequest = TicketRequest(
                                        user_id = userId,
                                        showtime_id = showtimeId,
                                        seats = seats,
                                        food_drinks = foodDrinks,
                                        payment_method = "ZaloPay", // Phương thức thanh toán
                                        price = totalAmount
                                    )

                                    // Gửi yêu cầu lên server để đặt vé
                                    Log.d(
                                        "Payment",
                                        "Sending ticket data to server: $ticketRequest"
                                    )
                                    sendTicketData(ticketRequest)

                                    // Chuyển đến màn hình chính sau khi thanh toán thành công
                                    val intent = Intent(this@Payment, MainActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)  // Clear all activities above MainActivity
                                    intent.putExtra(
                                        "payment_status",
                                        "Thanh toán thành công"
                                    ) // Thêm thông báo vào Intent
                                    startActivity(intent)
                                }

                                override fun onPaymentCanceled(p0: String?, p1: String?) {
                                    // Thanh toán bị hủy
                                    Log.d("Payment", "Payment canceled.")
                                    Toast.makeText(
                                        this@Payment,
                                        "Thanh toán bị hủy",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                override fun onPaymentError(
                                    p0: ZaloPayError?,
                                    p1: String?,
                                    p2: String?
                                ) {
                                    // Xử lý lỗi thanh toán
                                    Log.e("Payment", "Payment error: ${p0?.let { }}, $p1, $p2")
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
                        // Thông báo lỗi nếu mã trả về không hợp lệ
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
                    // Log lỗi và thông báo người dùng
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

    // Gửi dữ liệu thanh toán lên server
    private fun sendTicketData(ticketRequest: TicketRequest) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Gửi request đến server để đặt vé
                Log.d("Payment", "Sending ticket data to server: $ticketRequest")
                val response = RetrofitClient.apiService.bookTicket(ticketRequest).execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val ticketResponse = response.body()
                        if (ticketResponse != null) {
                            // In log thông tin trả về từ server
                            Log.d("Payment", "Đặt vé thành công: ${ticketResponse.message}")
                            Log.d("Payment", "Thông tin vé: ${ticketResponse.ticket}")
                            Log.d("Payment", "Ghế đã chọn: ${ticketResponse.seats}")
                            Log.d("Payment", "Món ăn/đồ uống: ${ticketResponse.food_drinks}")
                            Log.d("Payment", "Giá vé: ${ticketResponse.price}")

                            // Hiển thị thông báo hoặc chuyển hướng sang màn hình khác
                            // VD: Chuyển đến màn hình chính sau khi thanh toán thành công
                            val intent = Intent(this@Payment, MainActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)  // Clear all activities above MainActivity
                            intent.putExtra(
                                "payment_status",
                                "Thanh toán thành công"
                            ) // Thêm thông báo vào Intent
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