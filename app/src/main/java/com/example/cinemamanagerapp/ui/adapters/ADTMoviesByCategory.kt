package com.example.cinemamanagerapp.ui.adapters
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.model.MovieInfo
import com.example.cinemamanagerapp.ui.activities.Movie

open class ADTMoviesByCategory(private val mList: List<MovieInfo>, private val context : Context?) : RecyclerView.Adapter<ADTMoviesByCategory.ViewHold>() {
    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_info, parent, false)
        return ViewHold(view)
    }
    override fun getItemCount(): Int {
        Log.d("soluong", "getItemCount: ")
        return 10
        //if (mList == null) 0 else mList.size
    }
    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        var CL_MovieInfoContainer = holder.itemView.findViewById<ConstraintLayout>(R.id.CL_MovieInfoContainer);
        CL_MovieInfoContainer.setOnClickListener({context?.startActivity(Intent(context,Movie::class.java))})
        var img = holder.itemView.findViewById<ImageView>(R.id.Img_ImageMovie)
        var moviename = holder.itemView.findViewById<TextView>(R.id.tvMovieName);
        var tvShowtime = holder.itemView.findViewById<TextView>(R.id.tvShowtime);
    }

}