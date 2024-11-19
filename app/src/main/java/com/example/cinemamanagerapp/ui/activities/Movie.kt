package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.FavoriteCheckResponse
import com.example.cinemamanagerapp.api.MovieIdRequest
import com.example.cinemamanagerapp.api.MovieResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private lateinit var tvCategory: TextView
    private lateinit var imgMovie: ImageView
    private lateinit var btnAddToFavorites: TextView // Nút yêu thích (TextView)
    private lateinit var btnBooking: Button
    private var userId: Int = -1 // Để lấy từ SharedPreferences
    private var movieId: Int = -1 // Để lấy từ Intent

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
        tvCategory = findViewById(R.id.tvGenre)
        imgMovie = findViewById(R.id.videoMovie)
        btnAddToFavorites = findViewById(R.id.ig_Love) // Nút yêu thích (TextView)
        btnBooking = findViewById(R.id.BTN_BookTickets)

        // Lấy userId từ SharedPreferences
        userId = getUserId()

        // Lấy thông tin movie từ Intent
        val movieInfo: MovieResponse? = intent.getSerializableExtra("MOVIE_INFO") as? MovieResponse

        if (movieInfo != null) {
            movieId = movieInfo.movie_id // Lấy movieId từ movieInfo (từ Intent)

            tvTitle.text = movieInfo.title
            tvDescription.text = "Mô tả: ${movieInfo.description}"
            tvShowTime.text = "Giờ chiếu: ${formatDate(movieInfo.release_date)}"
            tvDuration.text = "Thời lượng: ${movieInfo.duration} phút"
            tvRating.text = "Xếp hạng: ${movieInfo.movie_id}"  // Kiểm tra việc lấy movie_id
            tvReleaseDate.text = "Ngày phát hành: ${formatDate(movieInfo.release_date)}"
            tvCast.text = "Diễn viên: ${movieInfo.description}"
            tvCategory.text = "Thể loại: ${movieInfo.category_id}"

            Glide.with(this).load(movieInfo.image_url).into(imgMovie)
        } else {
            Toast.makeText(this, "Không thể tải thông tin phim", Toast.LENGTH_SHORT).show()
            finish() // Nếu không có movieInfo, quay lại hoặc thoát
        }

        // Kiểm tra nếu bộ phim đã được thêm vào yêu thích của người dùng
        if (userId != -1 && movieId != -1) {
            checkIfMovieIsFavorite(userId, movieId)
        }

        // Sự kiện nhấn nút yêu thích
        btnAddToFavorites.setOnClickListener {
            if (btnAddToFavorites.text == "Thêm vào yêu thích") {
                addMovieToFavorites(userId, movieId)
            } else {
                removeMovieFromFavorites(userId, movieId)
            }
        }

        btnBooking.setOnClickListener{
            val intent = Intent(this, Payment::class.java)
            startActivity(intent)
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: return dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            dateString
        }
    }

    private fun checkIfMovieIsFavorite(userId: Int, movieId: Int) {
        val call = RetrofitClient.apiService.checkIfFavorite(userId, movieId)

        call.enqueue(object : Callback<FavoriteCheckResponse> {
            override fun onResponse(
                call: Call<FavoriteCheckResponse>,
                response: Response<FavoriteCheckResponse>
            ) {
                if (response.isSuccessful) {
                    val isFavorite = response.body()?.isFavorite ?: false
                    if (isFavorite) {
                        btnAddToFavorites.text = "Đã thêm vào yêu thích"
                        btnAddToFavorites.setTextColor(resources.getColor(R.color.green))
                    } else {
                        btnAddToFavorites.text = "Thêm vào yêu thích"
                        btnAddToFavorites.setTextColor(resources.getColor(R.color.blue_bld))
                    }
                } else {
                    Toast.makeText(this@Movie, "Lỗi khi kiểm tra yêu thích", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<FavoriteCheckResponse>, t: Throwable) {
                Toast.makeText(this@Movie, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addMovieToFavorites(userId: Int, movieId: Int) {
        if (movieId == -1) {  // Kiểm tra giá trị movieId hợp lệ
            Toast.makeText(this@Movie, "ID phim không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Movie", "Movie ID to add: $movieId")

        // Gọi API để thêm phim vào danh sách yêu thích
        val call = RetrofitClient.apiService.addFavoriteMovie(userId, movieId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Movie, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                    btnAddToFavorites.text = "Đã yêu thích"
                    btnAddToFavorites.setTextColor(resources.getColor(R.color.red))
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Movie", "Error: ${response.code()} - ${response.message()} - $errorBody")
                    Toast.makeText(
                        this@Movie,
                        "Thêm vào yêu thích thất bại. Lỗi: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Movie, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun removeMovieFromFavorites(userId: Int, movieId: Int) {
        if (movieId == -1) {  // Kiểm tra giá trị movieId hợp lệ
            Toast.makeText(this@Movie, "ID phim không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Movie", "Movie ID to remove: $movieId")

        // Gọi API để xóa phim khỏi danh sách yêu thích của người dùng
        val call = RetrofitClient.apiService.removeFavoriteMovie(userId, movieId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Movie, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
                    btnAddToFavorites.text = "Thêm vào yêu thích"
                    btnAddToFavorites.setTextColor(resources.getColor(R.color.blue_bld))
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Movie", "Error: ${response.code()} - ${response.message()} - $errorBody")
                    Toast.makeText(
                        this@Movie,
                        "Xóa khỏi yêu thích thất bại. Lỗi: $errorBody",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Movie, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // Lấy userId từ SharedPreferences
    private fun getUserId(): Int {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1)  // Trả về -1 nếu không tìm thấy user_id
    }
}
