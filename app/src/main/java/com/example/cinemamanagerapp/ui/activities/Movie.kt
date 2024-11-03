package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.MovieResponse
import java.text.SimpleDateFormat
import java.util.Locale

class Movie : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvShowTime: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvCast: TextView
    private lateinit var tvCategory: TextView // Thêm biến cho category
    private lateinit var imgMovie: ImageView
    private lateinit var btnBookTickets: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        // Ánh xạ các View
        tvTitle = findViewById(R.id.tvMovieName)
        tvDescription = findViewById(R.id.tvPlot)
        tvShowTime = findViewById(R.id.tvShowtime)
        tvDuration = findViewById(R.id.tvDuration)
        tvRating = findViewById(R.id.tvRating)
        tvReleaseDate = findViewById(R.id.tvReleaseDate)
        tvCast = findViewById(R.id.tvCast)
        tvCategory = findViewById(R.id.tvGenre) // Ánh xạ cho category
        imgMovie = findViewById(R.id.videoMovie) // Sửa id cho ImageView nếu cần
        btnBookTickets = findViewById(R.id.BTN_BookTickets)

        // Nhận thông tin phim từ Intent
        val movieInfo: MovieResponse? = intent.getSerializableExtra("MOVIE_INFO") as? MovieResponse

        movieInfo?.let {
            tvTitle.text = it.title
            tvDescription.text = "Mô tả: ${it.description}"
            tvShowTime.text = "Giờ chiếu: ${formatDate(it.release_date)}" // Định dạng giờ chiếu
            tvDuration.text = "Thời lượng: ${it.duration} phút"
            tvRating.text = "Xếp hạng: ${it.movie_id}" // Thay thế với xếp hạng nếu có
            tvReleaseDate.text = "Ngày phát hành: ${formatDate(it.release_date)}"
            tvCast.text = "Diễn viên: ${it.description}" // Thay thế với danh sách diễn viên nếu có
            tvCategory.text = "Thể loại: ${it.category_id}" // Hiển thị category

            Glide.with(this).load(it.image_url).into(imgMovie)
        } ?: run {
            Toast.makeText(this, "Không thể tải thông tin phim", Toast.LENGTH_SHORT).show()
        }

        btnBookTickets.setOnClickListener {
            val intent = Intent(this, ChooseChair::class.java)
            intent.putExtra("MOVIE_INFO", movieInfo) // Đảm bảo movieInfo không null
            startActivity(intent)
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: return dateString) // Trả về chuỗi gốc nếu không parse được
        } catch (e: Exception) {
            e.printStackTrace()
            dateString // Trả về chuỗi gốc nếu có lỗi
        }
    }
}
