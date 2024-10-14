package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.ui.adapters.ADTChairNumber

class ChooseChair : AppCompatActivity( ){
    private lateinit var rcvChairNumber : RecyclerView //btn_to_payment
    private lateinit var btn_to_payment : Button //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choose_chair)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcvChairNumber = findViewById(R.id.rcv_ChairList);
        rcvChairNumber.adapter = ADTChairNumber(Array(10) { Array(10) { false } });
        rcvChairNumber.layoutManager = GridLayoutManager(this,10)

        btn_to_payment = findViewById(R.id.btn_to_payment)
        btn_to_payment.setOnClickListener({startActivity(Intent(this,Payment::class.java))})
    }
}