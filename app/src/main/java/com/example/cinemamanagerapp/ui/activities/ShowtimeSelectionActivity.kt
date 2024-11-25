package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.MovieResponse
import com.example.cinemamanagerapp.api.ShowTimeResponse

class ShowtimeSelectionActivity : AppCompatActivity() {

    private lateinit var tvMovieTitle: TextView
    private lateinit var showtimeRecyclerView: RecyclerView
    private var selectedShowtime: ShowTimeResponse? = null
    private var movieInfo: MovieResponse? = null
    private var showtimes: List<ShowTimeResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showtime_selection)

        tvMovieTitle = findViewById(R.id.tvMovieTitle)
        showtimeRecyclerView = findViewById(R.id.showtime_list)

        // Nhận thông tin phim và danh sách suất chiếu từ Intent
        movieInfo = intent.getSerializableExtra("MOVIE_INFO") as? MovieResponse
        showtimes = intent.getSerializableExtra("SHOWTIMES") as? List<ShowTimeResponse>

        // Hiển thị tên phim
        tvMovieTitle.text = movieInfo?.title ?: "Movie Title"

        // Kiểm tra nếu danh sách suất chiếu không rỗng
        if (showtimes != null && showtimes!!.isNotEmpty()) {
            val adapter = Showtime_Adapter(showtimes!!) { selectedShowtime ->
                // Đặt suất chiếu đã chọn và lưu lại showtime_id
                this.selectedShowtime = selectedShowtime

                // Chuyển dữ liệu ngay lập tức khi một suất chiếu được chọn
                val intent = Intent(this, ChooseChair::class.java)
                // Chỉ truyền showtime_id, ticket_price, reserved_seats và movie_info
                intent.putExtra("SHOWTIME_ID", selectedShowtime.showtime_id)
                intent.putExtra("MOVIE_INFO", movieInfo)
                intent.putExtra("TICKET_PRICE", selectedShowtime.ticket_price)  // Truyền giá vé
                intent.putExtra(
                    "RESERVED_SEATS",
                    ArrayList(selectedShowtime.reserved_seats)
                )  // Truyền danh sách ghế đã đặt

                startActivity(intent)
            }
            showtimeRecyclerView.layoutManager = LinearLayoutManager(this)
            showtimeRecyclerView.adapter = adapter
        } else {
            Toast.makeText(this, "Không có suất chiếu cho bộ phim này!", Toast.LENGTH_SHORT).show()
        }
    }
}

