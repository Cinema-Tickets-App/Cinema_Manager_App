package com.example.cinemamanagerapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.Ticket
import com.example.cinemamanagerapp.ui.adapters.MovieHistory_Adapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {

    private lateinit var lvHistoryList: ListView
    private lateinit var adapter: MovieHistory_Adapter
    private lateinit var ticketList: List<Ticket>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment
        val rootView = inflater.inflate(R.layout.fragment_history, container, false)

        lvHistoryList = rootView.findViewById(R.id.lv_HistoryList)

        // Lấy user_id từ SharedPreferences khi mở HistoryFragment
        val userId = getUserId()

        // Kiểm tra userId có hợp lệ không
        if (userId == -1) {
            Toast.makeText(
                activity,
                "User ID không hợp lệ. Vui lòng đăng nhập lại.",
                Toast.LENGTH_SHORT
            ).show()
            return rootView
        }

        // Gọi API để lấy lịch sử vé của người dùng
        fetchTicketHistory(userId)

        return rootView
    }

    // Lấy user_id từ SharedPreferences
    private fun getUserId(): Int {
        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences?.getInt("user_id", -1) ?: -1  // Trả về -1 nếu không có user_id
    }

    private fun fetchTicketHistory(userId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Gọi API để lấy dữ liệu vé từ server
                val response = RetrofitClient.apiService.getTicketHistory(userId)

                // Log toàn bộ phản hồi từ server để debug
                Log.d("HistoryFragment", "API response: ${response.toString()}")

                // Kiểm tra dữ liệu trả về
                ticketList = response  // response là danh sách Ticket

                // Log số lượng vé lấy được
                Log.d("HistoryFragment", "Tickets retrieved: ${ticketList.size}")

                // Khởi tạo adapter và set dữ liệu cho ListView
                adapter = MovieHistory_Adapter(activity as Context, ticketList)
                lvHistoryList.adapter = adapter

            } catch (e: Exception) {
                // Log và thông báo lỗi khi gọi API
                Log.e("HistoryFragment", "Error occurred while fetching ticket history: ${e.message}")
                Toast.makeText(activity, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show()

                // Log chi tiết exception để debug
                Log.e("HistoryFragment", "Exception: ", e)
            }
        }
    }
}
