package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.adapters.ADTMovieHistory

class History : AppCompatActivity() {
    //
    private lateinit var lv_HistoryList: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lv_HistoryList = findViewById(R.id.lv_HistoryList)
        lv_HistoryList.adapter = ADTMovieHistory(null);
    }
}