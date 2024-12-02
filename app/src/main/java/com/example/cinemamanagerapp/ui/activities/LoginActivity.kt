package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.LoginRequest
import com.example.cinemamanagerapp.api.LoginResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextAcc: AppCompatEditText
    private lateinit var editTextPass: AppCompatEditText
    private lateinit var buttonLogin: AppCompatButton
    private lateinit var textViewRegister: AppCompatTextView
    private lateinit var textViewForgotPassword: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (isUserLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_login)

            editTextAcc = findViewById(R.id.editText_Acc)
            editTextPass = findViewById(R.id.editText_Pass)
            buttonLogin = findViewById(R.id.ButtonLogin)
            textViewRegister = findViewById(R.id.textRegister)


            // Go To Quên Pass

            textViewForgotPassword = findViewById(R.id.textViewForgotPasswordActivity)
            textViewForgotPassword.setOnClickListener {
                startActivity(Intent(this, ForgotPasswordActivity::class.java))
                finish()
            }

            buttonLogin.setOnClickListener {
                val email = editTextAcc.text.toString()
                val password = editTextPass.text.toString()

                // Kiểm tra email và mật khẩu có hợp lệ không
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (isEmailValid(email) && isPasswordStrong(password)) {
                        loginUser(email, password)
                    } else {
                        Toast.makeText(this, "Email hoặc mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            }

            textViewRegister.setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        RetrofitClient.apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        // Lưu user_id vào SharedPreferences
                        saveUserId(it.id)

                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng nhập thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Thông tin đăng nhập không chính xác",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun saveUserId(userId: Int) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("user_id", userId)
        editor.putBoolean("is_logged_in", true)  // Lưu trạng thái đăng nhập
        editor.apply()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    // Kiểm tra email hợp lệ
    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Kiểm tra mật khẩu có đủ độ mạnh không
    private fun isPasswordStrong(password: String): Boolean {
        // Kiểm tra mật khẩu có ít nhất 8 ký tự
        if (password.length < 8) return false

        // Kiểm tra mật khẩu có ít nhất một chữ cái viết hoa
        if (!password.any { it.isUpperCase() }) return false

        // Kiểm tra mật khẩu có ít nhất một chữ cái viết thường
        if (!password.any { it.isLowerCase() }) return false

        // Kiểm tra mật khẩu có ít nhất một chữ số
        if (!password.any { it.isDigit() }) return false

        // Kiểm tra mật khẩu có ít nhất một ký tự đặc biệt
        val specialChars = "!@#$%^&*()-_+=<>?"
        if (!password.any { it in specialChars }) return false

        return true
    }
}
