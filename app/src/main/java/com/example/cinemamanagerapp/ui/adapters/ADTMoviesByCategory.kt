package com.example.cinemamanagerapp.ui.adapters

import MovieInfo
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.activities.Movie
import com.bumptech.glide.Glide

open class ADTMoviesByCategory(private var mList: MutableList<MovieInfo>, private val context: Context?) :
    RecyclerView.Adapter<ADTMoviesByCategory.ViewHold>() {

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieInfoContainer: ConstraintLayout = itemView.findViewById(R.id.CL_MovieInfoContainer)
        val image: ImageView = itemView.findViewById(R.id.Img_ImageMovie)
        val movieName: TextView = itemView.findViewById(R.id.tvMovieName)
        val tvShowtime: TextView = itemView.findViewById(R.id.tvShowtime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_info, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        val movie = mList[position]

        // Thiết lập thông tin phim
        holder.movieName.text = movie.title
        holder.tvShowtime.text = movie.show_time.toString() // Chuyển đổi định dạng nếu cần


        Glide.with(context!!)
            .load(movie.image)
            .into(holder.image)


        holder.movieInfoContainer.setOnClickListener {
            val intent = Intent(context, Movie::class.java)
            intent.putExtra("MOVIE_INFO", movie) // Gửi toàn bộ đối tượng MovieInfo
            context.startActivity(intent)
        }
    }

    fun updateMovies(movies: List<MovieInfo>) {
        mList.clear()
        mList.addAll(movies)
        notifyDataSetChanged()
    }
}
