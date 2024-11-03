package com.example.cinemamanagerapp.ui.adapters

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R

class ChairNumber_Adapter(private var mList: Array<Array<Boolean>>, private val onChairClick: (Int) -> Unit) :
    RecyclerView.Adapter<ChairNumber_Adapter.ViewHold>() {

    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSeat: ImageView = itemView.findViewById(R.id.Img_SeatCondition)
        // Add more views if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.seat_condition, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return mList.size * mList[0].size // Assume mList is a 2D array
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        val row = position / mList[0].size
        val col = position % mList[0].size

        holder.imgSeat.setImageResource(R.drawable.icon_chair)
        holder.imgSeat.imageTintList = if (mList[row][col]) {
            ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.red_exp)) // Ghế đã chọn
        } else {
            ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.white)) // Ghế trống
        }

        holder.itemView.setOnClickListener {
            onChairClick(position) // Call the click listener
        }
    }
}
