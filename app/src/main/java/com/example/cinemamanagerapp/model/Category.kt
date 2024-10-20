package com.example.cinemamanagerapp.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?
)
