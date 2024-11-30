package com.example.cinemamanagerapp.api

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val full_name: String,
    val phone_number: String,
    val email: String,
    val password: String
)



data class UserProfileUpdateRequest(
    val email: String,
    val full_name: String,
    val phone_number: String,
    val address: String,
    val age: Int,
    val gender: String,
    val avatar_url: String
)



data class PasswordUpdateRequest(
    val user_id: Int,
    val current_password: String,
    val new_password: String
)

data class ReviewRequest(
    val bookTicketId: Int,
    val comment: String,
    val rate: Int,
)

data class UserProfileEditRequest(
    val user_id: Int,
    val email: String,
    val username: String,
    val phone_number: String? = null,
    val age: Int? = null,
    val address: String? = null,
    val gender: String,
)

data class MovieIdRequest(
    val movie_id: Int,
)


// Model cho yêu cầu đặt vé
data class TicketRequest(
    val user_id: Int,
    val showtime_id: Int,
    val seats: List<String>,
    val food_drinks: List<FoodDrink>,
    val payment_method: String,
    val price: Int
)
