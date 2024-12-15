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

//data class CloudinaryResponse(
//    val url: String?  // URL của ảnh sau khi tải lên
//)


// Model cho thông tin phim
data class MovieResponse(
    val movie_id: Int,
    val title: String,  // Tên phim
    val description: String,
    val trailer_url: String,
    val category_id: Int,
    val category: String,
    val duration: Int,
    val release_date: String,
    val image_url: String,
    val showtime: String? // Thêm thuộc tính này nếu cần
) : Serializable

// Model cho thông báo
data class NotificationResponse(
    val userId: Int,   // Thêm userId
    val title: String, // Tiêu đề thông báo
    val message: String, // Nội dung thông báo
    val timestamp: String // Thời gian hoặc có thể dùng Long cho thời gian
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
    val user: User,     // Thông tin người dùng (email)
    val showtime: ShowTimeResponse, // Thông tin suất chiếu
    val movie: MovieDetails,  // Thông tin về bộ phim
    val price: String?
)


data class User(
    val username: String? = "Tên người dùng không xác định", // Đảm bảo name có giá trị mặc định
    val email: String
)

data class ShowTimeResponse(
    val showtime_id: Int, // ID tự động trong hệ thống
    val movie_id: Int, // ID của phim
    val start_time: String, // Thời gian bắt đầu suất chiếu
    val room_id: Int, // ID phòng chiếu
    val ticket_price: Int, // Giá vé
    val movie: MovieDATA,
    val room: CinemaRoomResponse, // Thông tin phòng chiếu
    val reserved_seats: List<String>
) : Serializable

data class CinemaRoomResponse(
    val room_id: Int, // ID phòng chiếu (số)
    val room_name: String, // Tên phòng chiếu
    val seat_capacity: Int, // Số ghế
    val is_active: Boolean, // Phòng còn hoạt động không
    val start_time: String, // Thời gian bắt đầu của phòng
    val end_time: String, // Thời gian kết thúc của phòng
    val duration: Int, // Thời lượng của phòng
    val movie_id: Int // ID phim đang chiếu trong phòng
) : Serializable

data class MovieDATA(
    val _id: String, // ID phim (string trong JSON)
    val title: String, // Tên phim
    val description: String, // Mô tả phim
    val trailer_url: String, // Đường dẫn trailer phim
    val category_id: Int, // ID thể loại phim
    val duration: Int, // Thời lượng phim
    val release_date: String, // Ngày phát hành
    val image_url: String, // Đường dẫn ảnh phim
    val coming_soon: Boolean, // Có phải phim sắp chiếu không
    val movie_id: Int // ID phim
) : Serializable

// Thông tin chi tiết về phim
data class MovieDetails(
    val title: String,
    val description: String,
    val trailer_url: String,
    val image_url: String
)



// Model cho món ăn/đồ uống trong yêu cầu đặt vé
data class FoodDrink(
    val food_drink_id: Int,
    val quantity: Int
)

data class FavoriteCheckResponse(
    val isFavorite: Boolean
)

// Model for the API response after booking a ticket
data class TicketResponse(
    val message: String,
    val ticket: Ticket,  // Assuming the response contains a single ticket object
    val seats: List<String>,  // List of selected seats
    val food_drinks: List<FoodDrinksResponse>,  // List of food and drink items
    val price: String,  // Total price for the booking
    val room_name: String, // Tên phòng chiếu
)
data class FoodItem(
    val name: String,       // Tên món ăn
    val quantity: Int,     // Số lượng
    val price: String      // Giá món ăn (có thể là chuỗi để bao gồm cả "VND")
)

//Giảm giá
data class PromotionResponse(
    val promotion_id: Int,
    val discount_percentage: Int,
    val discount_code: String
)



