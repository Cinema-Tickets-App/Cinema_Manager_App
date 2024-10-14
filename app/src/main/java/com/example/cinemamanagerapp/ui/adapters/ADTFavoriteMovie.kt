package com.example.cinemamanagerapp.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.model.MovieInfo

class ADTFavoriteMovie(private val mList: List<MovieInfo>,private var context : Context?) : ADTMoviesByCategory(mList,context);