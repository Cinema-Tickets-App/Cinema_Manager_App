package com.example.cinemamanagerapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.RetrofitClient
import okhttp3.FormBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var backToLoginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_activity)

        emailEditText = findViewById(R.id.editText_Email)
        sendButton = findViewById(R.id.ButtonSendReset)
        backToLoginTextView = findViewById(R.id.textBackToLogin)

        sendButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isNotEmpty()) {
                sendResetPasswordRequest(email)
            } else {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
            }
        }

        backToLoginTextView.setOnClickListener {
            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun sendResetPasswordRequest(email: String) {
        val requestBody = FormBody.Builder()
            .add("email", email)
            .build()

        RetrofitClient.apiService.sendForgotPasswordRequest("reset-password/forgot-password", requestBody)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Yêu cầu reset mật khẩu đã được gửi!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Có lỗi xảy ra"
                        runOnUiThread {
                            Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Lỗi kết nối, vui lòng thử lại", Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
}
