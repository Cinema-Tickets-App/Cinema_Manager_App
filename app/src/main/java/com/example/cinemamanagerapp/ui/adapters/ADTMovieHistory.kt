package com.example.cinemamanagerapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.model.Notificaton

class ADTMovieHistory(private val movieList: List<Notificaton>?) :
    BaseAdapter() {
    override fun getCount(): Int {
        return 7
        // if (movieList == null) 0 else movieList.size
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = LayoutInflater.from(p2?.context).inflate(R.layout.movie_history, null);
        return view
    }
}