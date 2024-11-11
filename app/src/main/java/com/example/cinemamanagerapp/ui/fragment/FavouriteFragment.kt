package com.example.cinemamanagerapp.ui.fragment

import FavoriteMovie_Adapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.MovieResponse
import com.example.cinemamanagerapp.api.RetrofitClient

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteFragment : Fragment() {

    private lateinit var rcvFavoriteMovie: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        rcvFavoriteMovie = view.findViewById(R.id.rcv_FavoriteMovie)

        // Cấu hình RecyclerView
        rcvFavoriteMovie.layoutManager = GridLayoutManager(requireContext(), 2)

        // Lấy user_id từ SharedPreferences
        val userId = getUserId()

        if (userId != null) {
            // Lấy danh sách phim yêu thích từ API
            getFavoriteMovies(userId)
        } else {
            Toast.makeText(requireContext(), "Không tìm thấy ID người dùng", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    // Lấy user_id từ SharedPreferences
    private fun getUserId(): Int? {
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", -1).takeIf { it != -1 }  // Trả về null nếu không tìm thấy user_id
    }

    // Hàm lấy danh sách phim yêu thích của người dùng
    private fun getFavoriteMovies(userId: Int) {
        RetrofitClient.apiService.getFavoriteMovies(userId).enqueue(object : Callback<List<MovieResponse>> {
            override fun onResponse(call: Call<List<MovieResponse>>, response: Response<List<MovieResponse>>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (movies != null && movies.isNotEmpty()) {
                        // Chuyển List thành MutableList trước khi truyền cho Adapter
                        val mutableMovies = movies.toMutableList()
                        // Cập nhật adapter với danh sách phim yêu thích
                        val adapter = FavoriteMovie_Adapter(mutableMovies, requireContext())
                        rcvFavoriteMovie.adapter = adapter
                    } else {
                        Toast.makeText(requireContext(), "Không có phim yêu thích", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Lỗi khi tải phim yêu thích", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<MovieResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


