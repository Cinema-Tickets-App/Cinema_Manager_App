package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.MovieResponse
import com.example.cinemamanagerapp.ui.adapters.ChairNumber_Adapter
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChooseChair : AppCompatActivity() {

    private lateinit var rcvChairNumber: RecyclerView
    private lateinit var btnToPayment: Button
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvSelectedSeats: TextView
    private lateinit var chairList: Array<Array<Boolean>>
    private var ticketPrice: Int = 0
    private lateinit var movieInfo: MovieResponse
    private lateinit var reservedSeats: List<String>
    private var showtimeId: Int = -1
    private val selectedSeats = mutableListOf<String>()
    private lateinit var roomName: String // Thêm biến này

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_chair)

        val btnBack = findViewById<AppCompatImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Phương thức này sẽ xử lý quay lại màn hình trước đó
        }

        tvTotalPrice = findViewById(R.id.tvPaymentSum)
        tvSelectedSeats = findViewById(R.id.tvSelectedSeats)

        showtimeId = intent.getIntExtra("SHOWTIME_ID", -1)
        if (showtimeId == -1) {
            Toast.makeText(this, "Lỗi: Suất chiếu không hợp lệ", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val tvShowTime = findViewById<TextView>(R.id.tvTime) // Kết nối với TextView hiển thị thời gian

        // Lấy dữ liệu từ Intent
        val startTime = intent.getStringExtra("START_TIME") // Nhận chuỗi thời gian
        if (!startTime.isNullOrEmpty()) {
            val formattedTime = formatStartTime(startTime) // Định dạng thời gian
            tvShowTime.text = "Thời gian chiếu: $formattedTime" // Hiển thị lên TextView
        } else {
            tvShowTime.text = "Không có thông tin thời gian chiếu"
        }

        roomName = intent.getStringExtra("ROOM_NAME") ?: "Không xác định" // Nhận ROOM_NAME từ Intent

        // Nếu bạn cần hiển thị ROOM_NAME trên giao diện, bạn có thể thêm TextView và hiển thị nó
        val tvRoomName = findViewById<TextView>(R.id.tvRoomName)
        tvRoomName.text = "Phòng chiếu: $roomName"

        movieInfo = intent.getSerializableExtra("MOVIE_INFO") as MovieResponse
        ticketPrice = intent.getIntExtra("TICKET_PRICE", 0)

        reservedSeats = intent.getStringArrayListExtra("RESERVED_SEATS")?.map { it.trim() } ?: emptyList()

        chairList = Array(10) { Array(5) { false } }

        reservedSeats.forEach { seat ->
            val row = seat[0].uppercaseChar() - 'A'
            val colStr = seat.substring(1)
            val col = colStr.toIntOrNull() ?: 0

            if (row in 0..9 && col in 1..5) {
                chairList[row][col - 1] = true
            }
        }

        rcvChairNumber = findViewById(R.id.rcv_ChairList)
        rcvChairNumber.adapter = ChairNumber_Adapter(chairList, reservedSeats) { position ->
            val row = position / chairList[0].size
            val col = position % chairList[0].size
            if (!reservedSeats.contains("${(row + 'A'.toInt()).toChar()}${col + 1}")) {
                chairList[row][col] = !chairList[row][col]
                rcvChairNumber.adapter?.notifyItemChanged(position)
                updateSelectedSeatLabel(row, col)
                updateTotalPrice()
            }
        }
        rcvChairNumber.layoutManager = GridLayoutManager(this, 5)

        btnToPayment = findViewById(R.id.btn_to_payment)
        btnToPayment.setOnClickListener {
            val totalPrice = calculateTotalPrice() // Tính tổng tiền
            val selectedSeatsList = getSelectedSeats()

            // Kiểm tra nếu không có ghế nào được chọn
            if (selectedSeatsList.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một ghế", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Log thông tin khi chuyển dữ liệu thanh toán
            Log.d("ChooseChair", "Selected Seats: $selectedSeatsList")
            Log.d("ChooseChair", "Ticket Price: $ticketPrice")
            Log.d("ChooseChair", "Total Price: $totalPrice")

            val intent = Intent(this, Payment::class.java)
            intent.putExtra("TOTAL_AMOUNT", totalPrice) // Truyền tổng số tiền
            intent.putExtra("SELECTED_SEATS", selectedSeatsList)
            intent.putExtra("SHOWTIME_ID", showtimeId)
            intent.putExtra("TICKET_PRICE", ticketPrice)
            intent.putExtra("ROOM_NAME", roomName) // Truyền ROOM_NAME sang Payment

            // Log để xác nhận dữ liệu đã được truyền đi
            Log.d(
                "ChooseChair",
                "Passing data to Payment screen: Total Amount = $totalPrice, Selected Seats = $selectedSeatsList, Showtime ID = $showtimeId, Room Name = $roomName"
            )

            startActivity(intent)
        }
    }

    private fun updateSelectedSeatLabel(row: Int, col: Int) {
        val seatLabel = seatLabelFromIndex(row, col)
        if (chairList[row][col]) {
            selectedSeats.add(seatLabel)
        } else {
            selectedSeats.remove(seatLabel)
        }
        tvSelectedSeats.text = "Ghế đã chọn: ${selectedSeats.joinToString(", ")}"
    }

    private fun calculateTotalPrice(): Int {
        var totalPrice = 0
        for (row in chairList.indices) {
            for (col in chairList[row].indices) {
                if (chairList[row][col] && !reservedSeats.contains(seatLabelFromIndex(row, col))) {
                    totalPrice += ticketPrice
                }
            }
        }
        return totalPrice
    }

    private fun updateTotalPrice() {
        val totalPrice = calculateTotalPrice()
        tvTotalPrice.text = "Tổng tiền: ${totalPrice}đ"
        btnToPayment.isEnabled = totalPrice > 0
    }

    private fun getSelectedSeats(): String {
        val selectedSeatsList = mutableListOf<String>()
        for (row in chairList.indices) {
            for (col in chairList[row].indices) {
                if (chairList[row][col] && !reservedSeats.contains(seatLabelFromIndex(row, col))) {
                    selectedSeatsList.add(seatLabelFromIndex(row, col))
                }
            }
        }
        return selectedSeatsList.joinToString(", ")
    }

    private fun seatLabelFromIndex(row: Int, col: Int): String {
        val rowChar = ('A'.toInt() + row).toChar()
        return "${rowChar}${col + 1}"
    }

    fun formatStartTime(startTime: String): String {
        return try {
            // Parse chuỗi ISO 8601
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Múi giờ UTC
            val date = inputFormat.parse(startTime)

            // Định dạng thời gian theo múi giờ địa phương
            val outputFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
            outputFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
            "Không xác định"
        }
    }
}