package com.example.cinemamanagerapp.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cinemamanagerapp.ui.activities.AccountActivity
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.UserProfileResponse
import com.example.cinemamanagerapp.ui.activities.ChangePasswordActivity
import com.example.cinemamanagerapp.ui.activities.HistoryActivity
import com.example.cinemamanagerapp.ui.activities.LoginActivity

import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback // Đảm bảo import từ retrofit2

class SettingFragment : Fragment() {
    private lateinit var tvNameUserSetting: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout cho fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        // Khởi tạo TextView để hiển thị tên người dùng
        tvNameUserSetting = view.findViewById(R.id.tvNameUserSetting)

        // Lấy tên người dùng từ SharedPreferences và thiết lập vào TextView
        loadUserName()

        // Tìm LinearLayout và thiết lập OnClickListener
        val lnAccount: LinearLayout = view.findViewById(R.id.lnAccount)
        lnAccount.setOnClickListener {
            val intent = Intent(activity, AccountActivity::class.java)
            startActivity(intent)
        }


        val lnChangePassword: LinearLayout = view.findViewById(R.id.lnChangePassword)
        lnChangePassword.setOnClickListener {
            context?.startActivity(Intent(context, ChangePasswordActivity::class.java))
        }

        // Tìm nút Đăng xuất và thiết lập OnClickListener
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            logoutUser()
        }

        return view
    }

    private fun loadUserName() {
        val sharedPreferences = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getInt("user_id", -1) // Lấy user_id từ SharedPreferences

        if (userId != null && userId != -1) {
            RetrofitClient.apiService.getProfile(userId).enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(call: Call<UserProfileResponse>, response: Response<UserProfileResponse>) {
                    if (response.isSuccessful) {
                        val userProfile = response.body()
                        userProfile?.let {
                            // Hiển thị thông tin người dùng
                            tvNameUserSetting.text = it.full_name // Hiển thị tên người dùng
                        }
                    } else {
                        Toast.makeText(context, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun logoutUser() {
        // Lấy SharedPreferences từ Activity
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Xóa tất cả dữ liệu lưu trữ trong SharedPreferences
        editor.clear()
        editor.apply()

        // Đăng xuất và chuyển về màn hình LoginActivity
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)

        // Kết thúc activity hiện tại
        requireActivity().finish()
    }

}
