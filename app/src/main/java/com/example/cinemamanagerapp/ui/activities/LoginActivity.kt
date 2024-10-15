package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.LoginRequest
import com.example.cinemamanagerapp.api.LoginResponse
import com.example.cinemamanagerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLoggedIn()
        // khong dung ham checkIfloggedIn , vì có thể 1 user khác đã login trên 1 thiết bị khác và đổi mk
        // khi đó mã không còn đúng
        setContentView(R.layout.activity_login)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerTextView = findViewById(R.id.registerTextView)

        // Kiểm tra xem người dùng đã đăng nhập hay chưa


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            loginUser(email, password)
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun checkLoggedIn() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password =
            sharedPreferences.getString("password", null) // Trả về null nếu không có giá trị
        if (password != null && email != null) {
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        val loginRequest = LoginRequest(email = email, password = password)
        RetrofitClient.apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body() //
                    loginResponse?.let {
                        saveUserInfo(email, password) // luu thong tin login
                        // lưu email va password lại để lần sau dùng login
                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng nhập thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("userId", loginResponse.userId)
                        startActivity(intent)
                        finish()
                    } ?: run {
                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng nhập thất bại: Không có dữ liệu trả về",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: response.message()
                    Toast.makeText(
                        this@LoginActivity,
                        "Đăng nhập thất bại: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Lỗi: ${t.message}", t)
                Toast.makeText(
                    this@LoginActivity,
                    "Đã xảy ra lỗi: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun saveUserInfo(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
        Log.d("LoginActivity", "User ID saved: ${email} --- ${password}")
    }
}
