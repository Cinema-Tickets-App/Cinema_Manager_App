package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.ui.adapters.CategoryList_Adapter
import com.example.cinemamanagerapp.ui.adapters.MoviesByCategory_Adapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Button
import android.widget.Toast
import android.util.Log
import androidx.appcompat.widget.AppCompatImageButton
import com.example.cinemamanagerapp.api.MovieResponse

class NowMovie : AppCompatActivity() {

    private lateinit var rcvMovieByCategory: RecyclerView
    private lateinit var btnNowShowing: Button
    private lateinit var btnComingSoon: Button
    private lateinit var moviesAdapter: MoviesByCategory_Adapter // Khai báo adapter mà không khởi tạo ngay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_now_movie)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnBack = findViewById<AppCompatImageButton>(R.id.btnBackNowMovie)
        btnBack.setOnClickListener {
            finish() // Phương thức này sẽ xử lý quay lại màn hình trước đó
        }

        rcvMovieByCategory = findViewById(R.id.rcvMovieByCategory)
        moviesAdapter = MoviesByCategory_Adapter(mutableListOf(), this)
        rcvMovieByCategory.adapter = moviesAdapter
        rcvMovieByCategory.layoutManager = GridLayoutManager(this, 2)

        btnNowShowing = findViewById(R.id.btnNowShowing)
        btnComingSoon = findViewById(R.id.btnComingSoon)

        btnNowShowing.setOnClickListener { fetchNowShowingMovies() }
        btnComingSoon.setOnClickListener { fetchComingSoonMovies() }

        // Lấy phim đang chiếu khi khởi động
        fetchNowShowingMovies()
    }

    private fun fetchNowShowingMovies() {
        RetrofitClient.apiService.getNowShowingMovies().enqueue(object : Callback<List<MovieResponse>> {
            override fun onResponse(call: Call<List<MovieResponse>>, response: Response<List<MovieResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { movies ->
                        moviesAdapter.updateMovies(movies) // Cập nhật danh sách phim
                        Log.d("NowMovie", "Lấy danh sách phim đang chiếu thành công: ${movies.size} phim")
                    }
                } else {
                    Toast.makeText(this@NowMovie, "Lỗi khi lấy phim đang chiếu", Toast.LENGTH_SHORT).show()
                    Log.e("NowMovie", "Lỗi khi lấy phim đang chiếu: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<MovieResponse>>, t: Throwable) {
                Log.e("NowMovie", "Lỗi mạng: ${t.message}")
                Toast.makeText(this@NowMovie, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchComingSoonMovies() {
        RetrofitClient.apiService.getComingSoonMovies().enqueue(object : Callback<List<MovieResponse>> {
            override fun onResponse(call: Call<List<MovieResponse>>, response: Response<List<MovieResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { movies ->
                        moviesAdapter.updateMovies(movies) // Cập nhật danh sách phim
                        Log.d("NowMovie", "Lấy danh sách phim sắp ra mắt thành công: ${movies.size} phim")
                    }
                } else {
                    Toast.makeText(this@NowMovie, "Lỗi khi lấy phim sắp ra mắt", Toast.LENGTH_SHORT).show()
                    Log.e("NowMovie", "Lỗi khi lấy phim sắp ra mắt: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<MovieResponse>>, t: Throwable) {
                Log.e("NowMovie", "Lỗi mạng: ${t.message}")
                Toast.makeText(this@NowMovie, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
