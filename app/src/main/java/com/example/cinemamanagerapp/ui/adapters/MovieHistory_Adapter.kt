package com.example.cinemamanagerapp.ui.adapters

import android.content.Context
import android.content.Intent
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

        // Lấy tên phim từ ticket.movie (không phải ticket.showtime.movie)
        val movieName =
            ticket.movie?.title ?: "Tên phim không có" // Giá trị mặc định nếu không có tên phim
        tvMovieName.text = "Tên phim: $movieName"  // Hiển thị tên phim

        // Hiển thị danh sách ghế (ví dụ: A1, B1, C3, ...) trong TextView
        val seats =
            ticket.seats.joinToString(", ")  // Kết hợp danh sách ghế thành chuỗi, phân tách bằng dấu phẩy
        tvTickAmount.text = "Ghế: $seats"  // Hiển thị tên ghế

        // Xử lý và hiển thị thời gian chiếu
        val dateString = ticket.showtime.start_time // "2024-10-30T14:30:00.000Z"
        val date = convertStringToDate(dateString)
        val formattedDate = formatDate(date)
        tvShowTime.text = "Công chiếu: $formattedDate"  // Thời gian chiếu đã được định dạng

        // Hiển thị phương thức thanh toán
        tvPaymentSum.text = "Thanh toán: ${ticket.payment_method}"  // Phương thức thanh toán

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
            intent.putExtra("QR_CODE_CONTENT", qrCodeContent)  // Truyền mã QR vào intent
            intent.putExtra("MOVIE_NAME", ticket.movie?.title)  // Truyền tên phim vào intent
            intent.putExtra("TICKET_ID", ticket._id)  // Truyền ID vé vào intent
            context.startActivity(intent)
        }


        return view
    }

    // Chuyển đổi chuỗi thời gian ISO 8601 thành Date
    private fun convertStringToDate(dateString: String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return sdf.parse(dateString) ?: Date()  // Nếu không thể parse, trả về ngày hiện tại
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

