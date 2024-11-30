package com.example.cinemamanagerapp.ui.activities

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.ShowTimeResponse
import java.text.SimpleDateFormat
import java.util.*

class Showtime_Adapter(
    private val showtimes: List<ShowTimeResponse>,
    private val onItemClick: (ShowTimeResponse) -> Unit
) : RecyclerView.Adapter<Showtime_Adapter.ShowtimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowtimeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_showtime, parent, false)
        return ShowtimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowtimeViewHolder, position: Int) {
        val showTime = showtimes[position]
        holder.bind(showTime)
    }

    override fun getItemCount(): Int = showtimes.size

    inner class ShowtimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val startTimeTextView: TextView = view.findViewById(R.id.tvStartTime)
        private val roomTextView: TextView = view.findViewById(R.id.tvRoom)
        private val priceTextView: TextView = view.findViewById(R.id.tvTicketPrice)

        fun bind(showTime: ShowTimeResponse) {
            // Format thời gian hiển thị
            val formattedStartTime = formatTime(showTime.start_time)
            startTimeTextView.text = "Giờ: $formattedStartTime" // Hiển thị theo yêu cầu
            roomTextView.text = "Phòng: ${showTime.room}"
            priceTextView.text = "${showTime.ticket_price} VND"

            // Log để hiển thị dữ liệu
            Log.d("Showtime_Adapter", "Showtime ID: ${showTime.showtime_id}")
            Log.d("Showtime_Adapter", "Start Time: ${showTime.start_time}")
            Log.d("Showtime_Adapter", "Room: ${showTime.room}")
            Log.d("Showtime_Adapter", "Ticket Price: ${showTime.ticket_price}")
            Log.d(
                "Showtime_Adapter",
                "Reserved Seats: ${showTime.reserved_seats.joinToString(", ")}"
            )

            // Bắt sự kiện click vào mỗi item
            itemView.setOnClickListener {
                onItemClick(showTime)
            }
        }

        // Hàm để định dạng thời gian
        private fun formatTime(time: String): String {
            val inputFormat = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.getDefault()
            ) // Định dạng ISO 8601
            val outputFormat = SimpleDateFormat(
                "HH:mm dd/MM/yyyy",
                Locale.getDefault()
            ) // Định dạng hiển thị mong muốn

            return try {
                val date = inputFormat.parse(time)
                if (date != null) {
                    outputFormat.format(date) // Trả về thời gian đã format
                } else {
                    time // Trả về chuỗi gốc nếu không parse được
                }
            } catch (e: Exception) {
                // Nếu không thể parse, trả về chuỗi ban đầu
                time
            }
        }
    }
}
