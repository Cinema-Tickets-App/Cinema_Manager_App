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
    val user_id: Int,
    val email: String,
    val full_name: String,
    val phone_number: String,
    val address: String,
    val gender: String,
    val age: Int
)


data class PasswordUpdateRequest(
    val user_id: Int,
    val current_password: String,
    val new_password: String
)

data class ReviewRequest(
    val bookTicketId : Int,
    val comment: String,
    val rate : Int,
)

data class UserProfileEditRequest(
    val user_id : Int,
    val email: String,
    val username: String,
    val phone_number: String? = null,
    val age : Int? = null,
    val address: String? = null,
    val gender: String,
)