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

class ADTChairNumber(private var mList: Array<Array<Boolean>>) : RecyclerView.Adapter<ADTChairNumber.ViewHold>() {
    class ViewHold(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHold {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.seat_condition, parent, false)
        return ViewHold(view)
    }

    override fun getItemCount(): Int {
        return 100;
       // if (mList == null) 0 else mList.size
    }

    override fun onBindViewHolder(holder: ViewHold, position: Int) {
        Log.d("position", ": "+position)
        var img = holder.itemView.findViewById<ImageView>(R.id.Img_SeatCondition)
        val y = position/10;
        val x = position % 10;// vd 7 => 0/7 , 23 => 2/3, 20 => 2/0, 0 => 0/0
//        if(mList[x][y] == false){
//            img.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.red_exp))
//        }
        if(position % 2 == 0){
            img.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.red_exp))
        }
    }
}