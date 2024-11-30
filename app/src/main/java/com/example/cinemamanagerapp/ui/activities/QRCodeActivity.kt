package com.example.cinemamanagerapp.ui.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.FoodItem
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        // Trong onCreate của Activity
        val btnBack = findViewById<AppCompatButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Quay lại màn hình trước đó
            finish()
        }


        // Nhận thông tin từ Intent
        val qrCodeContent = intent.getStringExtra("QR_CODE_CONTENT") ?: ""
        val movieName = intent.getStringExtra("MOVIE_NAME") ?: "Tên phim không có"
        val ticketId = intent.getStringExtra("TICKET_ID") ?: "Không có ID vé"
        val userName = intent.getStringExtra("USER_EMAIL") ?: "Người dùng không xác định"
        val showTime = intent.getStringExtra("SHOW_TIME") ?: "Không có thông tin giờ chiếu"
        val seatNumbers = intent.getStringExtra("SEAT_NUMBERS") ?: "Chưa chọn ghế"
        val foodDetails = intent.getStringExtra("FOOD_DETAILS") ?: "Không đặt đồ ăn"
        val totalPrice = intent.getStringExtra("TOTAL_PRICE") ?: "0 VND"
        val paymentMethod = intent.getStringExtra("PAYMENT_METHOD") ?: "Chưa chọn"

        // Khởi tạo các view
        val ivQRCode: ImageView = findViewById(R.id.ivQRCode)
        val tvMovieName: TextView = findViewById(R.id.tvMovieInfo)
        val tvTicketInfo: TextView = findViewById(R.id.tvTicketInfo)
        val tvUserName: TextView = findViewById(R.id.tvNameInfo)
        val tvShowTime: TextView = findViewById(R.id.tvTimeInfo)
        val tvSeatNumbers: TextView = findViewById(R.id.tvChooseInfo)
        val tvFoodDetails: TextView = findViewById(R.id.tvFoodInfo)
        val tvTotalPrice: TextView = findViewById(R.id.tvPayInfo)
        val tvPaymentMethod: TextView = findViewById(R.id.tvPayListInfo)

        // Điền dữ liệu vào các view
        tvMovieName.text = "Phim: $movieName"
        tvTicketInfo.text = "ID Đặt Vé: $ticketId"
        tvUserName.text = "Email: $userName"
        tvShowTime.text = "Thời Gian Chiếu: $showTime"
        tvSeatNumbers.text = "Ghế: $seatNumbers"
        tvTotalPrice.text = "Tổng Giá: $totalPrice VNĐ"
        tvPaymentMethod.text = "Hình Thức Thanh Toán: $paymentMethod"

        // Xử lý thông tin đồ ăn
        tvFoodDetails.text = formatFoodDetails(foodDetails)

        // Tạo mã QR
        val qrCodeBitmap = generateQRCode(qrCodeContent)
        ivQRCode.setImageBitmap(qrCodeBitmap)
    }

    // Hàm format thông tin đồ ăn
    private fun formatFoodDetails(foodDetails: String): String {
        val formattedDetails = StringBuilder()
        if (foodDetails != "Không đặt đồ ăn") {
            // Chuyển chuỗi JSON thành mảng các đối tượng FoodItem
            val items = Gson().fromJson(foodDetails, Array<FoodItem>::class.java)

            // Lặp qua các món ăn trong mảng và thêm thông tin vào chuỗi
            items.forEach { item ->
                formattedDetails.append("${item.name} (x${item.quantity}) - ${item.price} VND\n")
            }
        } else {
            formattedDetails.append("Không có món ăn")
        }
        return formattedDetails.toString()
    }


    // Hàm tạo mã QR từ chuỗi
    private fun generateQRCode(content: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }

        return bmp
    }
}

