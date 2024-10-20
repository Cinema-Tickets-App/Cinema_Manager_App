package com.example.cinemamanagerapp.ui.adapters

import MovieInfo
import android.content.Context

class ADTFavoriteMovie(private var mList: MutableList<MovieInfo>, private var context: Context?) :
    ADTMoviesByCategory(mList, context) {

    fun updateFavoriteMovies(favoriteMovies: List<MovieInfo>) {
        mList.clear()
        mList.addAll(favoriteMovies)
        notifyDataSetChanged()
    }
}
