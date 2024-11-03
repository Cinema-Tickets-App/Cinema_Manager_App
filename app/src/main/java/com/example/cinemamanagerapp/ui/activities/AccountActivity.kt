package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.ApiService
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.UserProfileResponse
import com.example.cinemamanagerapp.api.UserProfileUpdateRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountActivity : AppCompatActivity() {

    private lateinit var imgUserAccount: ImageView
    private lateinit var btnBackAccount: ImageButton
    private lateinit var btnSaveAccount: Button
    private lateinit var tilEmailAccount: TextInputLayout
    private lateinit var tilNameAccount: TextInputLayout
    private lateinit var tilPhoneAccount: TextInputLayout
    private lateinit var tilAddress: TextInputLayout
    private lateinit var tilAgeAccount: TextInputLayout // Thêm biến cho tuổi
    private lateinit var rdoGroupGender: RadioGroup

    private lateinit var tedEmailAccount: TextInputEditText
    private lateinit var tedNameAccount: TextInputEditText
    private lateinit var tedPhoneAccount: TextInputEditText
    private lateinit var tedAddress: TextInputEditText
    private lateinit var tedAgeAccount: TextInputEditText // Thêm biến cho TextInputEditText tuổi

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // Khởi tạo các view
        imgUserAccount = findViewById(R.id.imgUserAccount)
        btnBackAccount = findViewById(R.id.btnBackAccount)
        btnSaveAccount = findViewById(R.id.btnSaveAccount)
        tilEmailAccount = findViewById(R.id.tilEmailAccount)
        tilNameAccount = findViewById(R.id.tilNameAccount)
        tilPhoneAccount = findViewById(R.id.tilPhoneAccount)
        tilAddress = findViewById(R.id.tilAddress)
        tilAgeAccount = findViewById(R.id.tilAgeAccount) // Khởi tạo tilAgeAccount
        rdoGroupGender = findViewById(R.id.rdoGroupGender)

        tedEmailAccount = findViewById(R.id.tedEmailAccount)
        tedNameAccount = findViewById(R.id.tedNameAccount)
        tedPhoneAccount = findViewById(R.id.tedPhoneAccount)
        tedAddress = findViewById(R.id.tedAddress)
        tedAgeAccount = findViewById(R.id.tedAgeAccount) // Khởi tạo tedAgeAccount

        // Khởi tạo Retrofit API service
        apiService = RetrofitClient.apiService

        // Lấy thông tin người dùng
        getUserProfile()

        // Sự kiện khi nhấn nút quay lại
        btnBackAccount.setOnClickListener {
            finish()  // Trở lại activity trước đó
        }

        // Sự kiện khi nhấn nút lưu thông tin
        btnSaveAccount.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun getUserProfile() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1) // Lấy user_id từ SharedPreferences

        if (userId != -1) {
            apiService.getProfile(userId).enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { userProfile ->
                            tedEmailAccount.setText(userProfile.email)
                            tedNameAccount.setText(userProfile.full_name)
                            tedPhoneAccount.setText(userProfile.phone_number)
                            tedAddress.setText(userProfile.address)
                            tedAgeAccount.setText(userProfile.age.toString())

                            when (userProfile.gender) {
                                "Male" -> rdoGroupGender.check(R.id.rdoMale)
                                "Female" -> rdoGroupGender.check(R.id.rdoFemale)
                                "Other" -> rdoGroupGender.check(R.id.rdoOther)
                            }

                            // Hiển thị ảnh đại diện
                            if (!userProfile.avatar_url.isNullOrEmpty()) {
                                Glide.with(this@AccountActivity)
                                    .load(userProfile.avatar_url)
                                    .placeholder(R.drawable.icon_avatar) // Hình ảnh hiển thị khi tải
                                    .error(R.drawable.icon_avatar) // Hình ảnh hiển thị khi có lỗi
                                    .into(imgUserAccount)
                            } else {
                                Log.d("AvatarURL", "Avatar URL is null or empty")
                            }

                        }
                    } else {
                        Toast.makeText(
                            this@AccountActivity,
                            "Không thể tải thông tin người dùng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        this@AccountActivity,
                        "Lỗi kết nối: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUserProfile() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1) // Lấy user_id từ SharedPreferences

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy user_id", Toast.LENGTH_SHORT).show()
            return
        }

        val email = tedEmailAccount.text.toString()
        val fullName = tedNameAccount.text.toString()
        val phone = tedPhoneAccount.text.toString()
        val address = tedAddress.text.toString()
        val age = tedAgeAccount.text.toString().toIntOrNull()
            ?: 0 // Lấy tuổi, mặc định là 0 nếu không hợp lệ
        val gender = when (rdoGroupGender.checkedRadioButtonId) {
            R.id.rdoMale -> "Male"
            R.id.rdoFemale -> "Female"
            else -> "Other"
        }

        val updatedProfile = UserProfileUpdateRequest(
            user_id = userId,
            email = email,
            full_name = fullName,
            phone_number = phone,
            address = address,
            age = age, // Thêm tuổi vào yêu cầu cập nhật
            gender = gender
        )

        // Log JSON trước khi gửi
        logJsonUpdate(updatedProfile)

        // Gọi API mà không cần thêm userId vào URL
        apiService.updateProfile(updatedProfile).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AccountActivity, "Cập nhật thành công", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val errorBody = response.errorBody()?.string() // In ra lỗi
                    Log.e("UpdateProfile", "Response code: ${response.code()}, Error: $errorBody")
                    Toast.makeText(
                        this@AccountActivity,
                        "Không thể cập nhật thông tin",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AccountActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logJsonUpdate(userProfile: UserProfileUpdateRequest) {
        // Chuyển đổi đối tượng thành JSON
        val jsonObject = JSONObject()
        jsonObject.put("user_id", userProfile.user_id)
        jsonObject.put("email", userProfile.email)
        jsonObject.put("full_name", userProfile.full_name)
        jsonObject.put("phone_number", userProfile.phone_number)
        jsonObject.put("address", userProfile.address)
        jsonObject.put("age", userProfile.age) // Thêm tuổi vào log
        jsonObject.put("gender", userProfile.gender)

        // In ra toàn bộ JSON
        Log.d("UserProfileUpdate", "JSON to be sent: $jsonObject")
    }

}

