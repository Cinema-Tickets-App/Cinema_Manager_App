package com.example.cinemamanagerapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


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


    // Lấy tất cả các loại đồ ăn và thức uống có sẵn.
    @GET("food-drinks")
    fun getAllFoodDrink(): Call<List<FoodDrinksResponse>>

    // Phương thức thêm phim vào yêu thích
    @POST("movies/users/{user_id}/favorites/{movie_id}")
    fun addFavoriteMovie(
        @Path("user_id") userId: Int,   // Lấy user_id từ URL
        @Path("movie_id") movieId: Int  // Lấy movie_id từ URL
    ): Call<Void>


    // Phương thức xóa phim khỏi danh sách yêu thích
    @DELETE("movies/users/{user_id}/favorites/{movie_id}")
    fun removeFavoriteMovie(
        @Path("user_id") userId: Int,   // Lấy user_id từ URL
        @Path("movie_id") movieId: Int  // Lấy movie_id từ URL
    ): Call<Void>


    // Phương thức kiểm tra phim có trong danh sách yêu thích của người dùng không
    @GET("movies/users/{user_id}/favorites/{movie_id}")
    fun checkIfFavorite(
        @Path("user_id") userId: Int,
        @Path("movie_id") movieId: Int
    ): Call<FavoriteCheckResponse>


    // Lấy lịch sử đặt vé của người dùng dựa trên ID người dùng.
    @GET("/booking-history/history/{userId}")
    suspend fun getTicketHistory(@Path("userId") userId: Int): List<Ticket>

    // Lấy danh sách phim yêu thích của người dùng
    @GET("movies/users/{user_id}/favorites")
    fun getFavoriteMovies(
        @Path("user_id") userId: Int
    ): Call<List<MovieResponse>>

    @GET("showtimes/{movie_id}")
    fun getShowtimesByMovieId(
        @Path("movie_id") movieId: Int
    ): Call<List<ShowTimeResponse>>

    // Function to make a booking request
    @POST("tickets/book")
    fun bookTicket(@Body ticketRequest: TicketRequest): Call<TicketResponse>
}
