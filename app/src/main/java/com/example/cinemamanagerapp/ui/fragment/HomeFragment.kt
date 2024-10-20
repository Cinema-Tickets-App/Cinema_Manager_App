package com.example.cinemamanagerapp.ui.fragment

import MovieInfo
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.model.Category
import com.example.cinemamanagerapp.ui.activities.NowMovie
import com.example.cinemamanagerapp.ui.adapters.ADTCategoryList
import com.example.cinemamanagerapp.ui.adapters.ADTMoviesByCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var nameUserTextView: TextView
    private lateinit var rcvMovieCategoryList: RecyclerView
    private lateinit var rcvMovieByCategory: RecyclerView
    private lateinit var tv_ToNowMovie: TextView
    private lateinit var moviesAdapter: ADTMoviesByCategory
    private lateinit var categoryAdapter: ADTCategoryList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)

        nameUserTextView = view.findViewById(R.id.tvNameUser)
        tv_ToNowMovie = view.findViewById(R.id.tv_ToNowMovie)

        // Ánh xạ RecyclerView cho danh sách thể loại
        rcvMovieCategoryList = view.findViewById(R.id.rcvMovieCategoryList)
        rcvMovieCategoryList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Khởi tạo adapter cho danh sách thể loại
        categoryAdapter = ADTCategoryList(mutableListOf())
        rcvMovieCategoryList.adapter = categoryAdapter

        // Ánh xạ RecyclerView cho danh sách phim theo thể loại
        rcvMovieByCategory = view.findViewById(R.id.rcvMovieByCategory)
        moviesAdapter = ADTMoviesByCategory(mutableListOf(), context)
        rcvMovieByCategory.adapter = moviesAdapter
        rcvMovieByCategory.layoutManager = GridLayoutManager(context, 2)

        // Lấy danh sách thể loại
        fetchCategories()

        // Lấy danh sách phim
        fetchMovies()

        tv_ToNowMovie.setOnClickListener {
            // Chuyển đến NowMovie activity
            val intent = Intent(context, NowMovie::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun fetchCategories() {
        RetrofitClient.apiService.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        categoryAdapter.updateCategories(categories)
                    } ?: run {
                        Log.e("FetchCategories", "No categories found")
                    }
                } else {
                    Log.e("FetchCategories", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Failed to fetch categories: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.e("FetchCategories", "Error fetching categories", t)
                Toast.makeText(context, "Error fetching categories: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchMovies() {
        RetrofitClient.apiService.getMovies().enqueue(object : Callback<List<MovieInfo>> {
            override fun onResponse(call: Call<List<MovieInfo>>, response: Response<List<MovieInfo>>) {
                if (response.isSuccessful) {
                    response.body()?.let { movies ->
                        if (isAdded) {
                            moviesAdapter.updateMovies(movies)
                            Log.d("FetchMovies", "Movies fetched: $movies")
                        }
                    }
                } else {
                    Log.e("FetchMovies", "Response not successful: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Failed to fetch movies", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MovieInfo>>, t: Throwable) {
                Log.e("FetchMovies", "Error fetching movies", t)
                Toast.makeText(context, "Error fetching movies: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
