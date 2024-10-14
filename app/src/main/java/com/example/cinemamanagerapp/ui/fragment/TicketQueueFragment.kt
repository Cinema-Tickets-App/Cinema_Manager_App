package com.example.cinemamanagerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.adapters.ADTFavoriteMovie

class TicketQueueFragment : Fragment() {
    private lateinit var rcvFavoriteMovie : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticketqueue, container, false)
        rcvFavoriteMovie = view.findViewById(R.id.rcv_FavoriteMovie);
        rcvFavoriteMovie.adapter = ADTFavoriteMovie(mutableListOf(),context);
        rcvFavoriteMovie.layoutManager = GridLayoutManager(context,2)
        return view
    }
}
