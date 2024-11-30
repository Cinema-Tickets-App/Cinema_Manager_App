package com.example.cinemamanagerapp.ui.fragment

import android.content.Context
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
import com.example.cinemamanagerapp.api.CategoryResponse
import com.example.cinemamanagerapp.api.MovieResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.UserProfileResponse
import com.example.cinemamanagerapp.ui.activities.NowMovie
import com.example.cinemamanagerapp.ui.adapters.CategoryList_Adapter
import com.example.cinemamanagerapp.ui.adapters.MoviesByCategory_Adapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var nameUserTextView: TextView
    private lateinit var rcvMovieCategoryList: RecyclerView
    private lateinit var rcvMovieByCategory: RecyclerView
    private lateinit var tv_ToNowMovie: TextView
    private lateinit var moviesAdapter: MoviesByCategory_Adapter
    private lateinit var categoryAdapter: CategoryList_Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)

        nameUserTextView = view.findViewById(R.id.tvNameUser)
        tv_ToNowMovie = view.findViewById(R.id.tv_ToNowMovie)

        rcvMovieCategoryList = view.findViewById(R.id.rcvMovieCategoryList)
        rcvMovieCategoryList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        categoryAdapter = CategoryList_Adapter(mutableListOf()) { categoryId ->
            fetchMoviesByCategory(categoryId) // Gọi API lấy phim theo thể loại
        }
        rcvMovieCategoryList.adapter = categoryAdapter

        rcvMovieByCategory = view.findViewById(R.id.rcvMovieByCategory)
        moviesAdapter = MoviesByCategory_Adapter(mutableListOf(), requireContext())
        rcvMovieByCategory.adapter = moviesAdapter
        rcvMovieByCategory.layoutManager = GridLayoutManager(requireContext(), 2)

        fetchCategories()
        fetchMovies()

        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId != -1) {
            fetchUserProfile(userId)
        }

        tv_ToNowMovie.setOnClickListener {
            val intent = Intent(requireContext(), NowMovie::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun fetchMoviesByCategory(category_id: Int) {
        RetrofitClient.apiService.getMoviesByGenre(category_id).enqueue(object : Callback<List<MovieResponse>> {
            override fun onResponse(call: Call<List<MovieResponse>>, response: Response<List<MovieResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { movies ->
                        moviesAdapter.updateMovies(movies)
                    } ?: run {
                        Log.e("FetchMoviesByCategory", "Không tìm thấy phim cho thể loại này")
                    }
                } else {
                    Log.e("FetchMoviesByCategory", "Phản hồi không thành công: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Không tìm thấy phim cho thể loại này", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MovieResponse>>, t: Throwable) {
                Log.e("FetchMoviesByCategory", "Lỗi khi lấy phim theo thể loại", t)
                Toast.makeText(requireContext(), "Lỗi khi lấy phim theo thể loại: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCategories() {
        RetrofitClient.apiService.getCategories().enqueue(object : Callback<List<CategoryResponse>> {
            override fun onResponse(call: Call<List<CategoryResponse>>, response: Response<List<CategoryResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        categoryAdapter.updateCategories(categories)
                    } ?: run {
                        Log.e("FetchCategories", "Không tìm thấy thể loại")
                    }
                } else {
                    Log.e("FetchCategories", "Phản hồi không thành công: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Không thể lấy danh sách thể loại: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                Log.e("FetchCategories", "Lỗi khi lấy thể loại", t)
                Toast.makeText(requireContext(), "Lỗi khi lấy thể loại: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchMovies() {
        RetrofitClient.apiService.getMovies().enqueue(object : Callback<List<MovieResponse>> {
            override fun onResponse(call: Call<List<MovieResponse>>, response: Response<List<MovieResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let { movies ->
                        if (isAdded) {
                            moviesAdapter.updateMovies(movies)
                        }
                    }
                } else {
                    Log.e("FetchMovies", "Phản hồi không thành công: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Không thể lấy danh sách phim", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MovieResponse>>, t: Throwable) {
                Log.e("FetchMovies", "Lỗi khi lấy danh sách phim", t)
                Toast.makeText(requireContext(), "Lỗi khi lấy danh sách phim: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchUserProfile(userId: Int) {
        RetrofitClient.apiService.getProfile(userId).enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { userProfile ->
                        nameUserTextView.text = userProfile.full_name
                    } ?: run {
                        Log.e("FetchUserProfile", "Không tìm thấy hồ sơ người dùng")
                    }
                } else {
                    Log.e("FetchUserProfile", "Phản hồi không thành công: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Không thể lấy hồ sơ người dùng", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("FetchUserProfile", "Lỗi khi lấy hồ sơ người dùng", t)
                Toast.makeText(requireContext(), "Lỗi khi lấy hồ sơ người dùng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
