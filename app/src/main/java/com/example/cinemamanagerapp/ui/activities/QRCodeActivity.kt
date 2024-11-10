package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Bitmap
import android.widget.Toast

class QRCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        // Lấy thông tin từ Intent
        val qrCodeContent = intent.getStringExtra("QR_CODE_CONTENT") ?: ""
        val movieName = intent.getStringExtra("MOVIE_NAME") ?: "Tên phim không có"
        val ticketId = intent.getStringExtra("TICKET_ID") ?: "Không có ID vé"

        // Khởi tạo các view
        val ivQRCode: ImageView = findViewById(R.id.ivQRCode)
        val tvMovieName: TextView = findViewById(R.id.tvMovieName)
        val tvTicketInfo: TextView = findViewById(R.id.tvTicketInfo)
        val btnBack: Button = findViewById(R.id.btnBack)

        // Cập nhật thông tin tên phim và vé
        tvMovieName.text = "Tên phim: $movieName"
        tvTicketInfo.text = "Vé ID: $ticketId"

        // Tạo mã QR
        val qrCodeBitmap = generateQRCode(qrCodeContent)
        ivQRCode.setImageBitmap(qrCodeBitmap)

        // Xử lý sự kiện quay lại
        btnBack.setOnClickListener {
            onBackPressed()  // Quay lại màn hình trước đó
        }
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
