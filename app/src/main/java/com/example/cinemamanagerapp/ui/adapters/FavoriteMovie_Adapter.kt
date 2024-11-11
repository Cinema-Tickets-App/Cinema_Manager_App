import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.MovieResponse
import java.text.SimpleDateFormat
import java.util.*

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

        // Chuyển đổi và hiển thị ngày chiếu
        val releaseDate = movie.release_date
        if (releaseDate != null) {
            val formattedDate = formatDate(releaseDate)
            holder.tvShowtime.text = "Ngày chiếu: $formattedDate"
        } else {
            holder.tvShowtime.text = "Không có thông tin"
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateFavoriteMovies(favoriteMovies: List<MovieResponse>) {
        mList.clear()
        mList.addAll(favoriteMovies)
        notifyDataSetChanged()
    }

    // Hàm chuyển đổi ngày từ ISO string thành định dạng mong muốn
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())  // Định dạng ngày ISO
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())  // Định dạng ngày bạn muốn hiển thị

        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date)  // Trả về ngày theo định dạng bạn muốn
        } catch (e: Exception) {
            e.printStackTrace()
            dateString  // Nếu có lỗi trong việc phân tích, trả về chuỗi ngày gốc
        }
    }
}
