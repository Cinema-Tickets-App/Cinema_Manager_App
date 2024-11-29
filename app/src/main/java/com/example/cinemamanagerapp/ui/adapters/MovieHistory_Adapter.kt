package com.example.cinemamanagerapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.Ticket
import com.example.cinemamanagerapp.ui.activities.QRCodeActivity
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class MovieHistory_Adapter(private val context: Context, private val ticketList: List<Ticket>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return ticketList.size
    }

    override fun getItem(position: Int): Any {
        return ticketList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Inflate layout
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.movie_history, parent, false)

        // Lấy vé từ danh sách
        val ticket = ticketList[position]

        // Khai báo các View trong layout
        val tvMovieName: TextView = view.findViewById(R.id.tv_movieName)
        val tvTickAmount: TextView = view.findViewById(R.id.tv_tickAmount)
        val tvShowTime: TextView = view.findViewById(R.id.tv_showTime)
        val tvPaymentSum: TextView = view.findViewById(R.id.tv_paymentSum)
        val ivMoviePoster: ImageView =
            view.findViewById(R.id.img_movie)  // ImageView để hiển thị ảnh

        // Lấy tên phim từ ticket.movie
        val movieName =
            ticket.movie?.title ?: "Tên phim không có" // Nếu không có tên phim, hiển thị mặc định
        tvMovieName.text = "Tên phim: $movieName"  // Hiển thị tên phim

        // Hiển thị danh sách ghế
        val seats = ticket.seats.joinToString(", ")  // Kết hợp danh sách ghế thành chuỗi
        tvTickAmount.text = "Ghế: $seats"  // Hiển thị tên ghế

        // Xử lý và hiển thị thời gian chiếu
        val dateString = ticket.showtime.start_time // Lấy thời gian chiếu từ ticket.showtime
        val date = convertStringToDate(dateString)
        val formattedDate = formatDate(date)
        tvShowTime.text = "Công chiếu: $formattedDate"  // Hiển thị thời gian chiếu đã định dạng

        // Hiển thị phương thức thanh toán
        tvPaymentSum.text = when {
            ticket.price == null -> "Thanh toán: Không có giá"
            ticket.price == "N/A" -> "Thanh toán: Không có giá"
            else -> "Thanh toán: ${ticket.price}đ"
        }

        // Tải ảnh phim từ URL và hiển thị vào ImageView sử dụng Glide
        val movieImageUrl = ticket.movie?.image_url ?: ""  // Lấy URL ảnh từ movie (nếu có)
        Glide.with(context)
            .load(movieImageUrl)  // URL của ảnh phim
            .placeholder(R.drawable.icon_avatar)  // Hình ảnh mặc định khi ảnh chưa tải xong
            .error(R.drawable.ic_google_logo)  // Hình ảnh hiển thị khi có lỗi tải ảnh
            .into(ivMoviePoster)  // Gán ảnh vào ImageView

        // Thêm sự kiện khi nhấn vào vé để mở QRCodeActivity
        view.setOnClickListener {
            val qrCodeContent = generateQRCode(ticket)  // Mã QR từ ticket
            val intent = Intent(context, QRCodeActivity::class.java)

            // Truyền thông tin cần thiết vào intent
            intent.putExtra("QR_CODE_CONTENT", qrCodeContent)  // Truyền mã QR vào intent
            intent.putExtra("MOVIE_NAME", ticket.movie?.title)  // Truyền tên phim vào intent
            intent.putExtra("TICKET_ID", ticket._id)// Truyền ID vé vào intent
            intent.putExtra(
                "USER_EMAIL",
                ticket.user?.email ?: "Email người dùng không có"
            )

            intent.putExtra(
                "SHOW_TIME",
                ticket.showtime?.start_time ?: "Không có thông tin giờ chiếu"
            )  // Truyền thời gian chiếu
            intent.putExtra(
                "SEAT_NUMBERS",
                ticket.seats.joinToString(", ") ?: "Chưa chọn ghế"
            )  // Truyền danh sách ghế

// Nếu food_drinks là một mảng, bạn cần phải chuyển nó thành chuỗi JSON hoặc truyền mảng nếu muốn
            val foodDetails = if (ticket.food_drinks != null && ticket.food_drinks.isNotEmpty()) {
                Gson().toJson(ticket.food_drinks)  // Chuyển mảng food_drinks thành chuỗi JSON
            } else {
                "Không đặt đồ ăn"
            }
            intent.putExtra("FOOD_DETAILS", foodDetails)  // Truyền thông tin món ăn
            intent.putExtra("TOTAL_PRICE", ticket.price ?: "Chưa có giá")  // Truyền tổng giá
            intent.putExtra(
                "PAYMENT_METHOD",
                ticket.payment_method ?: "Chưa chọn"
            )  // Truyền phương thức thanh toán

            // Mở QRCodeActivity với intent đã thêm dữ liệu
            context.startActivity(intent)
        }

        return view
    }

    // Chuyển đổi chuỗi thời gian ISO 8601 thành Date
    private fun convertStringToDate(dateString: String): Date {
        if (dateString == "N/A") {
            return Date() // Trả về ngày hiện tại nếu không có dữ liệu
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return try {
            sdf.parse(dateString) ?: Date()  // Nếu không parse được, trả về ngày hiện tại
        } catch (e: Exception) {
            Date() // Nếu có lỗi, trả về ngày hiện tại
        }
    }

    // Định dạng lại thời gian thành kiểu mong muốn (ví dụ: dd/MM/yyyy HH:mm:ss)
    private fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }

    // Hàm tạo mã QR ngẫu nhiên cho vé
    private fun generateQRCode(ticket: Ticket): String {
        // Bạn có thể tạo mã QR từ ID vé, hoặc thông tin khác
        return ticket._id ?: "QR_${System.currentTimeMillis()}"
    }
}
