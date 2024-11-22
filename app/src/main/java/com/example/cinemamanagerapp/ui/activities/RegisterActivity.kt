package com.example.cinemamanagerapp.ui.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.ApiService
import com.example.cinemamanagerapp.api.RegisterRequest
import com.example.cinemamanagerapp.api.RegisterResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextFullName: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private var txtLogin: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextFullName = findViewById(R.id.editText_Username)  // Thêm EditText cho tên đầy đủ
        editTextPhoneNumber = findViewById(R.id.editText_PhoneNumber)  // Thêm EditText cho số điện thoại
        editTextEmail = findViewById(R.id.editText_Email)
        editTextPassword = findViewById(R.id.editText_Password)
        editTextConfirmPassword = findViewById(R.id.editText_ConfirmPassword)
        btnRegister = findViewById(R.id.ButtonRegister)
        txtLogin = findViewById(R.id.textLogin)

        btnRegister.setOnClickListener {
            registerUser()
        }
        txtLogin?.setOnClickListener {
            finish()
        }
    }

    private fun registerUser() {
        val fullName = editTextFullName.text.toString().trim()
        val phoneNumber = editTextPhoneNumber.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        // Kiểm tra xem các trường có hợp lệ không
        if (fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Tất cả các trường là bắt buộc", Toast.LENGTH_SHORT).show()
            return
        }

        // Kiểm tra xem mật khẩu có khớp không
        if (password != confirmPassword) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
            return
        }

        // Tạo đối tượng đăng ký
        val registerRequest = RegisterRequest(fullName, phoneNumber, email, password)

        // Gọi API để đăng ký
        RetrofitClient.apiService.registerUser(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    // Có thể chuyển hướng đến màn hình khác
                } else {
                    Toast.makeText(this@RegisterActivity, "Đăng ký thất bại: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
