package com.example.cinemamanagerapp.models

class FoodDrink {
    private var _id: String? = null
    private var name: String? = null
    private var price: String? = null
    private var type: String? = null
    private var image: String? = null

    // Getter và setter cho các thuộc tính
    fun get_id(): String? {
        return _id
    }

    fun getName(): String? {
        return name
    }

    fun getPrice(): String? {
        return price
    }

    fun getType(): String? {
        return type
    }

    fun getImage(): String? {
        return image
    }
}