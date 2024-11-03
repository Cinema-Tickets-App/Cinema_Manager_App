package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.adapters.ChairNumber_Adapter

class ChooseChair : AppCompatActivity() {
    private lateinit var rcvChairNumber: RecyclerView
    private lateinit var btnToPayment: Button
    private lateinit var chairList: Array<Array<Boolean>> // Giả sử danh sách ghế được khởi tạo ở đây

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_chair)

        chairList = Array(10) { Array(10) { false } } // Khởi tạo ghế trống
        rcvChairNumber = findViewById(R.id.rcv_ChairList)
        rcvChairNumber.adapter = ChairNumber_Adapter(chairList) { position ->
            val row = position / chairList[0].size
            val col = position % chairList[0].size
            chairList[row][col] = !chairList[row][col] // Chuyển trạng thái ghế
            rcvChairNumber.adapter?.notifyItemChanged(position) // Cập nhật lại ghế
        }
        rcvChairNumber.layoutManager = GridLayoutManager(this, 5)

        btnToPayment = findViewById(R.id.btn_to_payment)
        btnToPayment.setOnClickListener {
            val totalPrice = calculateTotalPrice() // Hàm để tính tổng tiền
            val intent = Intent(this, Payment::class.java)
            intent.putExtra("TOTAL_PRICE", totalPrice) // Truyền tổng tiền vào intent
            startActivity(intent) // Chuyển sang màn thanh toán
        }
    }

    private fun calculateTotalPrice(): Int {
        // Tính tổng tiền dựa trên số ghế đã chọn
        return chairList.flatten().count { it } * 157000 // Giả sử giá mỗi ghế là 157000đ
    }
}
