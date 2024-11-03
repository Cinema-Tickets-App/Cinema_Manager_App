package com.example.cinemamanagerapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R // Import đúng lớp R
import com.example.cinemamanagerapp.api.MovieResponse

class FavoriteMovie_Adapter(
    private var mList: MutableList<MovieResponse>,
    private var context: Context
) : RecyclerView.Adapter<FavoriteMovie_Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieName: TextView = itemView.findViewById(R.id.tvMovieName)
        val movieImage: ImageView = itemView.findViewById(R.id.Img_ImageMovie)
        val tvShowtime: TextView = itemView.findViewById(R.id.tvShowtime) // Thêm nếu cần
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = mList[position]
        holder.movieName.text = movie.title
        Glide.with(context).load(movie.image_url).into(holder.movieImage)
        // Nếu bạn cần hiển thị thời gian chiếu, đảm bảo rằng movie có thuộc tính showtime
        holder.tvShowtime.text = "Showtime: ${movie.showtime ?: "Không có thông tin"}"
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateFavoriteMovies(favoriteMovies: List<MovieResponse>) {
        mList.clear()
        mList.addAll(favoriteMovies)
        notifyDataSetChanged()
    }
}
