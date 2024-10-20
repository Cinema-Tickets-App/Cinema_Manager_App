package com.example.cinemamanagerapp.ui.activities

import MovieInfo
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
import com.example.cinemamanagerapp.model.Category

import com.example.cinemamanagerapp.ui.adapters.ADTCategoryList
import com.example.cinemamanagerapp.ui.adapters.ADTMoviesByCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import android.util.Log

class NowMovie : AppCompatActivity() {
    private lateinit var rcvMovieCategoryList: RecyclerView
    private lateinit var rcvMovieByCategory: RecyclerView
    private lateinit var categoryAdapter: ADTCategoryList
    private lateinit var moviesAdapter: ADTMoviesByCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_now_movie)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcvMovieCategoryList = findViewById(R.id.rcvMovieCategoryList)
        categoryAdapter = ADTCategoryList(mutableListOf())
        rcvMovieCategoryList.adapter = categoryAdapter
        rcvMovieCategoryList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rcvMovieByCategory = findViewById(R.id.rcvMovieByCategory)
        moviesAdapter = ADTMoviesByCategory(mutableListOf(), this)
        rcvMovieByCategory.adapter = moviesAdapter
        rcvMovieByCategory.layoutManager = GridLayoutManager(this, 2)

        fetchCategories()
        fetchMovies()
    }

    private fun fetchCategories() {
        RetrofitClient.apiService.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        categoryAdapter.updateCategories(categories)
                    } ?: run {
                        Log.e("FetchCategories", "No categories found")
                    }
                } else {
                    Log.e(
                        "FetchCategories",
                        "Response not successful: ${response.errorBody()?.string()}"
                    )
                    Toast.makeText(
                        this@NowMovie,
                        "Failed to fetch categories: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("FetchCategories", "Error fetching categories", t)
                Toast.makeText(
                    this@NowMovie,
                    "Error fetching categories: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun fetchMovies() {
        RetrofitClient.apiService.getMovies().enqueue(object : Callback<List<MovieInfo>> {
            override fun onResponse(
                call: Call<List<MovieInfo>>,
                response: Response<List<MovieInfo>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { movies ->
                        moviesAdapter.updateMovies(movies)
                        Log.d("FetchMovies", "Movies fetched: $movies")
                    }
                } else {
                    Log.e(
                        "FetchMovies",
                        "Response not successful: ${response.errorBody()?.string()}"
                    )
                    Toast.makeText(this@NowMovie, "Failed to fetch movies", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<MovieInfo>>, t: Throwable) {
                Log.e("FetchMovies", "Error fetching movies", t)
                Toast.makeText(
                    this@NowMovie,
                    "Error fetching movies: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

//    private fun fetchNowShowingMovies() {
//        RetrofitClient.apiService.getNowShowingMovies().enqueue(object : Callback<List<MovieInfo>> {
//            override fun onResponse(call: Call<List<MovieInfo>>, response: Response<List<MovieInfo>>) {
//                if (response.isSuccessful) {
//                    response.body()?.let { movies ->
//                        moviesAdapter.updateMovies(movies)
//                        Log.d("FetchNowShowingMovies", "Movies fetched: $movies")
//                    }
//                } else {
//                    Log.e("FetchNowShowingMovies", "Response not successful: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<MovieInfo>>, t: Throwable) {
//                Log.e("FetchNowShowingMovies", "Error fetching now showing movies", t)
//            }
//        })
//    }

//    private fun fetchComingSoonMovies() {
//        RetrofitClient.apiService.getComingSoonMovies().enqueue(object : Callback<List<MovieInfo>> {
//            override fun onResponse(call: Call<List<MovieInfo>>, response: Response<List<MovieInfo>>) {
//                if (response.isSuccessful) {
//                    response.body()?.let { movies ->
//                        moviesAdapter.updateMovies(movies)
//                        Log.d("FetchComingSoonMovies", "Movies fetched: $movies")
//                    }
//                } else {
//                    Log.e("FetchComingSoonMovies", "Response not successful: ${response.errorBody()?.string()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<MovieInfo>>, t: Throwable) {
//                Log.e("FetchComingSoonMovies", "Error fetching coming soon movies", t)
//            }
//        })
//    }
//}

