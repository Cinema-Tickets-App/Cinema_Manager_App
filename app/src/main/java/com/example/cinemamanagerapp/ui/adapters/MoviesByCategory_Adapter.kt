package com.example.cinemamanagerapp.ui.adapters

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
import com.example.cinemamanagerapp.ui.activities.Movie // Kiểm tra lại đây có đúng không?
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.api.MovieResponse
import java.text.SimpleDateFormat
import java.util.Locale

class MoviesByCategory_Adapter(
    private var mList: MutableList<MovieResponse>,
    private val context: Context
) : RecyclerView.Adapter<MoviesByCategory_Adapter.ViewHold>() {

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

        holder.movieName.text = movie.title
        holder.tvShowtime.text = formatDate(movie.release_date)

        Glide.with(holder.image.context)
            .load(movie.image_url)
            .into(holder.image)

        holder.movieInfoContainer.setOnClickListener {
            val intent = Intent(context, Movie::class.java)
            intent.putExtra("MOVIE_INFO", movie)
            context.startActivity(intent)
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: return dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            dateString
        }
    }

    fun updateMovies(movies: List<MovieResponse>) {
        mList.clear()
        mList.addAll(movies)
        notifyDataSetChanged()
    }
}
