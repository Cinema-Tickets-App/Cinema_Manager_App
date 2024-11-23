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
    var quantity: Int // Thay đổi từ val sang var để có thể thay đổi số lượng
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

// Model cho thông tin phim
data class MovieResponse(
    val movie_id: Int,
    val title: String,  // Tên phim
    val description: String,
    val trailer_url: String,
    val category_id: Int,
    val duration: Int,
    val release_date: String,
    val image_url: String,
    val showtime: String? // Thêm thuộc tính này nếu cần
) : Serializable

// Model cho thông báo
data class NotificationResponse(
    val title: String,
    val message: String,
    val timestamp: String // Hoặc có thể dùng Long cho thời gian
)

// Model cho lịch sử đặt vé
data class BookingHistoryResponse(
    val status: String,
    val message: String,
    val data: TicketData? // Thông tin về lịch sử vé
)

// Model cho dữ liệu vé
data class TicketData(
    val tickets: List<Ticket>  // Danh sách vé
)

// Model cho thông tin vé
data class Ticket(
    val _id: String,         // ID của vé (dưới dạng String)
    val user_id: Int,        // ID của người dùng
    val showtime_id: Int,    // ID của suất chiếu
    val movie_id: Int,       // ID của phim
    val payment_method: String, // Phương thức thanh toán
    val qr_code: String,     // QR code của vé
    val flag: Int,           // Cờ đánh dấu trạng thái
    val seats: List<String>, // Danh sách các ghế đã chọn
    val food_drinks: List<FoodDrinksResponse>, // Danh sách món ăn/đồ uống
    val booking_time: String, // Thời gian đặt vé
    val book_tickets_id: Int, // ID của vé đặt
    val user: UserEmail,     // Thông tin người dùng (email)
    val showtime: ShowTimeResponse, // Thông tin suất chiếu
    val movie: MovieDetails,  // Thông tin về bộ phim
    val price: String?
)


// Model cho thông tin người dùng (email)
data class UserEmail(
    val email: String
)

// Model cho thông tin suất chiếu
data class ShowTimeResponse(
    val showtime_id: Int,      // ID của suất chiếu
    val start_time: String,    // Thời gian bắt đầu
    val room: String,          // Phòng chiếu
    val ticket_price: Int,     // Giá vé của suất chiếu
    val reserved_seats: List<String>  // Các ghế đã được đặt
)


// Thông tin chi tiết về phim
data class MovieDetails(
    val title: String,      // Tên phim
    val description: String, // Mô tả phim
    val trailer_url: String, // URL trailer
    val image_url: String   // URL ảnh đại diện phim
)

data class FavoriteCheckResponse(
    val isFavorite: Boolean
)

