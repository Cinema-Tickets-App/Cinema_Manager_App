package com.example.cinemamanagerapp.model

data class Food(
    var foodId: Int,
    var name: String,
    var price: Double,
    var image: String,
    var quantity: Int = 1
)
