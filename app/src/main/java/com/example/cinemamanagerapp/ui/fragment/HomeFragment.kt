package com.example.cinemamanagerapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.activities.MainActivity
import com.example.cinemamanagerapp.ui.activities.NowMovie
import com.example.cinemamanagerapp.ui.adapters.ADTCategoryList
import com.example.cinemamanagerapp.ui.adapters.ADTFavoriteMovie
import com.example.cinemamanagerapp.ui.adapters.ADTMoviesByCategory

class HomeFragment : Fragment() {
    private lateinit var nameUserTextView: TextView
    private lateinit var rcvMovieCategoryList: RecyclerView
    private lateinit var rcvMovieByCategory: RecyclerView //
    private lateinit var tv_ToNowMovie: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_fragment, container, false)
        nameUserTextView = view.findViewById(R.id.tvNameUser)
       // nameUserTextView.text = MainActivity.userName
        tv_ToNowMovie = view.findViewById(R.id.tv_ToNowMovie);
        tv_ToNowMovie.setOnClickListener(View.OnClickListener { startActivity(Intent(context,NowMovie::class.java)) })
        rcvMovieCategoryList = view.findViewById(R.id.rcvMovieCategoryList);
        rcvMovieCategoryList.adapter = ADTCategoryList(null)
        rcvMovieCategoryList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        rcvMovieByCategory = view.findViewById(R.id.rcvMovieByCategory);
        rcvMovieByCategory.adapter = ADTMoviesByCategory(mutableListOf(),context);
        rcvMovieByCategory.layoutManager = GridLayoutManager(context,2)
        return view
    }
}
