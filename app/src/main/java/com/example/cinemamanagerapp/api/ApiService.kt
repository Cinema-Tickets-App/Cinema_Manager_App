package com.example.cinemamanagerapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {

    // Xác thực người dùng với thông tin đăng nhập.
    @POST("auth/login")
    fun loginUser(@Body user: LoginRequest): Call<LoginResponse>

    // Đăng ký người dùng mới với các thông tin trong RegisterRequest.
    @POST("/auth/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    // Cập nhật mật khẩu người dùng dựa trên User_ID.
    @POST("auth/change-password")
    fun updatePassword(@Body passwordRequest: PasswordUpdateRequest): Call<Void>


    // Lấy thông tin hồ sơ người dùng qua ID.
    @GET("auth/user/{id}")
    fun getProfile(@Path("id") id: Int): Call<UserProfileResponse>

    @PUT("auth/update-info")
    fun updateProfile(@Body updatedProfile: UserProfileUpdateRequest): Call<Void>


    // Lấy tất cả danh mục phim dưới dạng danh sách chuỗi.
    @GET("categories")
    fun getAllCategory(): Call<List<String>>

    @GET("categories")
    fun getCategories(): Call<List<CategoryResponse>>

    @GET("movies")
    fun getMovies(): Call<List<MovieResponse>>

    // Lấy thông tin phim dựa trên ID của phim.
    @GET("movies/{id}")
    fun getMovieById(@Path("id") id: Int): Call<List<MovieResponse>>


    @GET("movies/upcoming")
    fun getNowShowingMovies(): Call<List<MovieResponse>>

    @GET("movies/coming-soon")
    fun getComingSoonMovies(): Call<List<MovieResponse>>

    // Lấy danh sách phim theo thể loại.
    @GET("movies/genre/{genreId}")
    fun getMoviesByGenre(@Path("genreId") genreId: Int): Call<List<MovieResponse>>


    // Lấy danh sách phim ngẫu nhiên để chiếu lên slide.
    @GET("api/movie/random")
    fun getRandomMovies(): Call<List<MovieResponse>>

//    // Lấy tất cả các suất chiếu cho một danh mục phim cụ thể.
//    @GET("/showtimes/{category}")
//    fun getAllShowTime(@Path("category") category: String): Call<List<ShowTimeResponse>>
//
//    // Lấy các suất chiếu trong ngày cho một danh mục phim cụ thể.
//    @GET("api/showtime/now/{category}")
//    fun getNowShowTime(@Path("category") category: String): Call<List<ShowTimeResponse>>

    // Lấy tất cả các loại đồ ăn và thức uống có sẵn.
    @GET("food-drinks")
    fun getAllFoodDrink(): Call<List<FoodDrinksResponse>>
//
//    // Lấy tất cả các đánh giá cho một phim dựa trên ID phim.
//    @GET("reviews/{movie-id}")
//    fun getAllReviewMovie(@Path("movie-id") movieId: Int): Call<List<ReviewResponse>>
//
//    // Gửi đánh giá cho một suất chiếu cụ thể với thông tin trong ReviewRequest.
//    @PUT("api/review/submit/{showtimeId}")
//    fun getSubmitReview(@Path("showtimeId") showtimeId: Int, @Body review: ReviewRequest): Call<Void>
//
//    // Xóa đánh giá của một phim dựa trên ID đánh giá.
//    @DELETE("api/review/delete/{reviewId}")
//    fun deleteReView(@Path("reviewId") reviewId: Int): Call<Void>
//
//    // Lấy lịch sử đặt vé của người dùng dựa trên ID người dùng.
//    @GET("api/history/{userId}")
//    fun getAllHistory(@Path("userId") userId: Int): Call<BookingHistoryResponse>
}
