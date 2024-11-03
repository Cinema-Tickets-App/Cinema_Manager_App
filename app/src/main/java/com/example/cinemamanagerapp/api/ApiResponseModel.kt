package com.example.cinemamanagerapp.api

import java.io.Serializable

// Model cho phản hồi đăng nhập
data class LoginResponse(
    val id: Int,
    val full_name: String,
    val phone_number: String?,
    val email: String,
    val active: Boolean,
    val created_at: String,  // định dạng ngày theo chuỗi
    val updated_at: String   // định dạng ngày theo chuỗi
)

// Model cho phản hồi đăng ký
data class RegisterResponse(
    val message: String,
    val userId: Int,
    val userName: String,
    val email: String,
    val createdAt: String // định dạng ngày theo chuỗi
)

data class FoodDrinksResponse(
    val food_drink_id: Int,
    val name: String,
    val type: String,
    val price: Double,
    val image: String?,
    var quantity: Int // Thay đổi từ val sang var
)


// Model cho đánh giá
data class ReviewResponse(
    val review_id: Int,
    val comment: String,
    val rate: Int
)

// Model cho thông tin thể loại
data class CategoryResponse(
    val category_id: Int,
    val name: String,
    val description: String? = null,
    val created_at: String,
    val updated_at: String? = null
)


// Model cho thông tin người dùng
data class UserProfileResponse(
    val user_id: Int,
    val email: String,
    val password: String,
    val full_name: String,
    val phone_number: String? = null,
    val age: Int? = null,
    val address: String? = null,
    val gender: String,
    val created_at: String,
    val last_login: String,
    val avatar_url: String
)




data class MovieResponse(
    val movie_id: Int,
    val title: String,
    val description: String,
    val trailer_url: String,
    val category_id: Int,
    val duration: Int,
    val release_date: String,
    val image_url: String,
    val showtime: String? // Thêm thuộc tính này nếu cần
) : Serializable

// Model cho thông tin lịch chiếu
data class ShowTimeResponse(
    val showtime_id: Int,
    val start_time: String,   // định dạng ngày theo chuỗi
    val room: String,
    val ticket_price: Int,
    val movie: ShowTimeMovieInfo
)

// Model phụ cho thông tin phim trong lịch chiếu
data class ShowTimeMovieInfo(
    val id: Int,
    val name: String
)

// Model cho lịch sử đặt vé
data class BookingHistoryResponse(
    val idShowTime: Int,
    val movieName: String,
    val ticketAmount: Int,
    val showTime: String,     // định dạng ngày theo chuỗi
    val payment: Int
)

data class NotificationResponse(
    val title: String,
    val message: String,
    val timestamp: String // Hoặc có thể dùng Long cho thời gian
)


