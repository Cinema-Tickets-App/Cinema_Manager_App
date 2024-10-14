package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.adapters.ADTCategoryList
import com.example.cinemamanagerapp.ui.adapters.ADTMoviesByCategory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NowMovie : AppCompatActivity() {
    private lateinit var rcvMovieCategoryList: RecyclerView
    private lateinit var rcvMovieByCategory: RecyclerView //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_now_movie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        rcvMovieCategoryList = findViewById(R.id.rcvMovieCategoryList);
        rcvMovieCategoryList.adapter = ADTCategoryList(null)
        rcvMovieCategoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rcvMovieByCategory = findViewById(R.id.rcvMovieByCategory);
        rcvMovieByCategory.adapter = ADTMoviesByCategory(mutableListOf(),this);
        rcvMovieByCategory.layoutManager = GridLayoutManager(this,2)
    }
}