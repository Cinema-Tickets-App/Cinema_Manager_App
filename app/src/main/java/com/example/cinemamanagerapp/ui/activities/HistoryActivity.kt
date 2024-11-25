package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.Ticket
import com.example.cinemamanagerapp.ui.adapters.MovieHistory_Adapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var lvHistoryList: ListView
    private lateinit var adapter: MovieHistory_Adapter
    private lateinit var ticketList: List<Ticket>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        lvHistoryList = findViewById(R.id.lv_HistoryList)

        // Lấy user_id từ SharedPreferences khi mở HistoryActivity
        val userId = getUserId()

        // Kiểm tra userId có hợp lệ không
        if (userId == -1) {
            Toast.makeText(
                this,
                "User ID không hợp lệ. Vui lòng đăng nhập lại.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Gọi API để lấy lịch sử vé của người dùng
        fetchTicketHistory(userId)
    }

    // Lấy user_id từ SharedPreferences
    private fun getUserId(): Int {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1)  // Trả về -1 nếu không có user_id
    }

    private fun fetchTicketHistory(userId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {  // Thay GlobalScope bằng lifecycleScope
            try {
                // Gọi API để lấy dữ liệu vé từ server
                val response = RetrofitClient.apiService.getTicketHistory(userId)

                // Log toàn bộ phản hồi từ server để debug
                Log.d("HistoryActivity", "API response: ${response.toString()}")

                // Kiểm tra dữ liệu trả về
                ticketList = response  // response là danh sách Ticket

                // Log số lượng vé lấy được
                Log.d("HistoryActivity", "Tickets retrieved: ${ticketList.size}")

                // Khởi tạo adapter và set dữ liệu cho ListView
                adapter = MovieHistory_Adapter(this@HistoryActivity, ticketList)
                lvHistoryList.adapter = adapter

            } catch (e: Exception) {
                // Log và thông báo lỗi khi gọi API
                Log.e("HistoryActivity", "Error occurred while fetching ticket history: ${e.message}")
                Toast.makeText(this@HistoryActivity, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show()

                // Log chi tiết exception để debug
                Log.e("HistoryActivity", "Exception: ", e)
            }
        }

    }


}


