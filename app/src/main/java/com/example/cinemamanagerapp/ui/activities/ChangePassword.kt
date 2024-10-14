package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cinemamanagerapp.R

class ChangePassword : AppCompatActivity() {
    private  lateinit var edt_passWord: EditText
    private lateinit var EDT_newPassword: EditText
    private lateinit var EDT_reNewPassword: EditText
    private lateinit var BTN_submit: Button
    private var accountPassword : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        edt_passWord = findViewById<EditText>(R.id.edt_passWord)
        EDT_newPassword = findViewById<EditText>(R.id.EDT_newPassword)
        EDT_reNewPassword = findViewById<EditText>(R.id.EDT_reNewPassword)
        BTN_submit = findViewById<Button>(R.id.BTN_submit)
        BTN_submit.setOnClickListener(View.OnClickListener { // chạy một luồng lên server để thay đổi thông tin
            val password: String = edt_passWord.getText().toString()
            val newPassword: String = EDT_newPassword.getText().toString()
            val reNewPassword: String = EDT_reNewPassword.getText().toString()
            if (password.length == 0 || newPassword.length == 0 || reNewPassword.length == 0) {
                Toast.makeText(this@ChangePassword, "Thông tin trống", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if(accountPassword == null){
                val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                accountPassword = sharedPreferences.getString("password", null)
            }

            if(password.equals(accountPassword) == false){
                Toast.makeText(this@ChangePassword, "Mật khẩu không bạn đã nhập không chính xác", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }

            if (newPassword != reNewPassword) {
                Toast.makeText(this@ChangePassword, "Lỗi ! Mật khẩu mới của bạn", Toast.LENGTH_LONG)
                    .show()
                return@OnClickListener
            }
        })
    }
}