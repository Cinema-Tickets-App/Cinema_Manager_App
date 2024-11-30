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
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.FavoriteCheckResponse
import com.example.cinemamanagerapp.api.MovieDetails
import com.example.cinemamanagerapp.api.MovieResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.ShowTimeResponse
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
    private lateinit var btnAddToFavorites: TextView
    private lateinit var btnBookTickets: Button
    private var btnTrailer: Button? = null
    private var userId: Int = -1
    private var movieId: Int = -1
    private var movieInfo: MovieResponse? = null  // Khai báo movieInfo ở phạm vi lớp
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val btnBack = findViewById<AppCompatImageButton>(R.id.btnBackMovie)
        btnBack.setOnClickListener {
            finish() // Phương thức này sẽ xử lý quay lại màn hình trước đó
        }



        // Ánh xạ các View
        tvTitle = findViewById(R.id.tvMovieName)
        tvDescription = findViewById(R.id.tvPlot)
        tvShowTime = findViewById(R.id.tvShowtime)
        tvDuration = findViewById(R.id.tvDuration)

        tvReleaseDate = findViewById(R.id.tvReleaseDate)

        tvCategory = findViewById(R.id.tvGenre)
        imgMovie = findViewById(R.id.videoMovie)
        btnAddToFavorites = findViewById(R.id.ig_Love)
        btnBookTickets = findViewById(R.id.BTN_BookTickets)
        btnTrailer = findViewById(R.id.btnTrailer)

        btnTrailer!!.setOnClickListener {
            val trailerUrl = movieInfo?.trailer_url // Lấy giá trị trailer_url từ đối tượng movieInfo
            if (trailerUrl != null) {
                val intent = Intent(this, TrailerActivity::class.java)
                intent.putExtra("url", trailerUrl) // Truyền URL trailer qua intent
                startActivity(intent)
            } else {
                Toast.makeText(this, "Trailer không khả dụng", Toast.LENGTH_SHORT).show()
            }
        }

        userId = getUserId()

        movieInfo =
            intent.getSerializableExtra("MOVIE_INFO") as? MovieResponse  // Gán giá trị movieInfo từ Intent

        if (movieInfo != null) {
            movieId = movieInfo!!.movie_id

            tvTitle.text = movieInfo!!.title
            tvDescription.text = "Mô tả: ${movieInfo!!.description}"
//            tvShowTime.text = "Giờ chiếu: ${formatDate(movieInfo!!.release_date)}"
            tvDuration.text = "Thời lượng: ${movieInfo!!.duration} phút"
            tvReleaseDate.text = "Ngày phát hành: ${formatDate(movieInfo!!.release_date)}"
            tvCategory.text = "Thể loại: ${movieInfo!!.category}"


            Glide.with(this).load(movieInfo!!.image_url).into(imgMovie)
        } else {
            Toast.makeText(this, "Không thể tải thông tin phim", Toast.LENGTH_SHORT).show()
            finish()
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

        // Sự kiện nhấn nút đặt vé
        btnBookTickets.setOnClickListener {
            if (movieId != -1) {
                checkShowtimesForMovie(movieId)
            } else {
                Toast.makeText(this@Movie, "ID phim không hợp lệ", Toast.LENGTH_SHORT).show()
            }
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

    // Kiểm tra xem bộ phim đã có trong danh sách yêu thích của người dùng chưa
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

    // Thêm phim vào yêu thích
    private fun addMovieToFavorites(userId: Int, movieId: Int) {
        if (movieId == -1) {  // Kiểm tra giá trị movieId hợp lệ
            Toast.makeText(this@Movie, "ID phim không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Movie", "Movie ID to add: $movieId")

        val call = RetrofitClient.apiService.addFavoriteMovie(userId, movieId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Movie, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                    btnAddToFavorites.text = "Đã yêu thích"
                    btnAddToFavorites.setTextColor(resources.getColor(R.color.red))
                } else {
                    Toast.makeText(this@Movie, "Thêm vào yêu thích thất bại", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Movie, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Xóa phim khỏi yêu thích
    private fun removeMovieFromFavorites(userId: Int, movieId: Int) {
        if (movieId == -1) {  // Kiểm tra giá trị movieId hợp lệ
            Toast.makeText(this@Movie, "ID phim không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Movie", "Movie ID to remove: $movieId")

        val call = RetrofitClient.apiService.removeFavoriteMovie(userId, movieId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Movie, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
                    btnAddToFavorites.text = "Thêm vào yêu thích"
                    btnAddToFavorites.setTextColor(resources.getColor(R.color.blue_bld))
                } else {
                    Toast.makeText(this@Movie, "Xóa khỏi yêu thích thất bại", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Movie, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkShowtimesForMovie(movieId: Int) {
        Log.d("MovieActivity", "Đang kiểm tra suất chiếu cho phim có ID: $movieId")
        val call = RetrofitClient.apiService.getShowtimesByMovieId(movieId)

        call.enqueue(object : Callback<List<ShowTimeResponse>> {
            override fun onResponse(
                call: Call<List<ShowTimeResponse>>,
                response: Response<List<ShowTimeResponse>>
            ) {
                Log.d("MovieActivity", "Nhận phản hồi từ API: ${response.code()}")

                when (response.code()) {
                    200 -> {
                        val showtimes = response.body()
                        if (showtimes != null && showtimes.isNotEmpty()) {
                            Log.d("MovieActivity", "Tìm thấy ${showtimes.size} suất chiếu.")

                            // Truyền danh sách các suất chiếu qua màn hình ShowtimeSelectionActivity
                            val intent = Intent(this@Movie, ShowtimeSelectionActivity::class.java)
                            intent.putExtra("MOVIE_INFO", movieInfo)  // Chuyển thông tin phim
                            intent.putExtra(
                                "SHOWTIMES",
                                ArrayList(showtimes)
                            )  // Chuyển danh sách suất chiếu

                            Log.d("MovieActivity", "Chuyển sang màn hình chọn suất chiếu")
                            startActivity(intent)  // Chuyển màn hình
                        } else {
                            Log.d(
                                "MovieActivity",
                                "Rất tiếc, hiện tại không có suất chiếu cho bộ phim này."
                            )
                            Toast.makeText(
                                this@Movie,
                                "Xin lỗi, hiện tại không có suất chiếu. Vui lòng quay lại sau nhé!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    400 -> {
                        Log.d("MovieActivity", "Thông báo lỗi từ server: ${response.message()}")
                        Toast.makeText(
                            this@Movie,
                            "Hình như có chút vấn đề với mã phim. Hãy thử lại nhé!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    404 -> {
                        Log.d("MovieActivity", "Không có suất chiếu cho phim này.")
                        Toast.makeText(
                            this@Movie,
                            "Không tìm thấy suất chiếu cho bộ phim này. Bạn có thể thử phim khác nhé!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    500 -> {
                        Log.d("MovieActivity", "Lỗi server từ API.")
                        Toast.makeText(
                            this@Movie,
                            "Lỗi server. Vui lòng thử lại sau nhé!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        Log.d("MovieActivity", "Lỗi không xác định: ${response.code()}")
                        Toast.makeText(
                            this@Movie,
                            "Chúng tôi đang gặp chút sự cố. Vui lòng thử lại sau.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<ShowTimeResponse>>, t: Throwable) {
                Log.e("MovieActivity", "Lỗi kết nối: ${t.message}", t)
                Toast.makeText(this@Movie, "Lỗi kết nối. Vui lòng thử lại sau!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    // Lấy userId từ SharedPreferences
    private fun getUserId(): Int {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1)  // Trả về -1 nếu không tìm thấy user_id
    }
}
