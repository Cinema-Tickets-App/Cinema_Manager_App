package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.ApiService
import com.example.cinemamanagerapp.api.PasswordUpdateRequest
import com.example.cinemamanagerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var edtPassword: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtReNewPassword: EditText
    private lateinit var btnSubmit: Button
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Khởi tạo các view từ bố cục XML
        edtPassword = findViewById(R.id.edt_passWord)
        edtNewPassword = findViewById(R.id.EDT_newPassword)
        edtReNewPassword = findViewById(R.id.EDT_reNewPassword)
        btnSubmit = findViewById(R.id.BTN_submit)

        // Khởi tạo API service từ Retrofit
        apiService = RetrofitClient.apiService

        // Xử lý sự kiện nhấn nút thay đổi mật khẩu
        btnSubmit.setOnClickListener {
            val currentPassword = edtPassword.text.toString()
            val newPassword = edtNewPassword.text.toString()
            val reNewPassword = edtReNewPassword.text.toString()

            if (validatePasswords(newPassword, reNewPassword)) {
                changePassword(currentPassword, newPassword)
            }
        }
    }

    private fun validatePasswords(newPassword: String, reNewPassword: String): Boolean {
        return if (newPassword == reNewPassword) {
            true
        } else {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun changePassword(currentPassword: String, newPassword: String) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy user_id", Toast.LENGTH_SHORT).show()
            Log.e("ChangePasswordActivity", "Không tìm thấy user_id trong SharedPreferences")
            return
        }

        val passwordRequest = PasswordUpdateRequest(
            user_id = userId,
            current_password = currentPassword,
            new_password = newPassword
        )

        Log.d("ChangePasswordActivity", "Gửi yêu cầu đổi mật khẩu với user_id: $userId")
        apiService.updatePassword(passwordRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Đổi mật khẩu thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("ChangePasswordActivity", "Đổi mật khẩu thành công cho user_id: $userId")
                    finish()
                } else {
                    Log.e("ChangePasswordActivity", "Không thể đổi mật khẩu: ${response.code()}")
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Không thể đổi mật khẩu",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ChangePasswordActivity", "Lỗi kết nối: ${t.message}")
                Toast.makeText(this@ChangePasswordActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
