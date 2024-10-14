package com.example.cinemamanagerapp.ui.activities.accounts

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.ApiService
import com.example.cinemamanagerapp.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountActivity : AppCompatActivity() {
    private lateinit var btnBackAccount : ImageButton
    private lateinit var btnSave: Button
    private lateinit var tedEmail: EditText
    private lateinit var tedName: EditText
    private lateinit var tedPhone: EditText
    private lateinit var rdoGroupGender: RadioGroup

    private val apiService: ApiService by lazy { RetrofitClient.apiService }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initViews()
       // getUserInfo()
        btnBackAccount.setOnClickListener({finish()})
        btnSave.setOnClickListener {
         //   updateProfile()
        }
    }

    private fun initViews() {
        btnBackAccount = findViewById(R.id.btnBackAccount)
        btnSave = findViewById(R.id.btnSaveAccount)
        tedEmail = findViewById(R.id.tedEmailAccount)
        tedName = findViewById(R.id.tedNameAccount)
        tedPhone = findViewById(R.id.tedPhoneAccount)
        rdoGroupGender = findViewById(R.id.rdoGroupGender)
    }

//    private fun getUserInfo() {
//        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        val username = sharedPreferences.getString("username", null)
//        val email = sharedPreferences.getString("email", null)
//        val phone = sharedPreferences.getString("phone_number", null)
//        val gender = sharedPreferences.getString("gender", "Other")
//
//        tedName.setText(username)
//        tedEmail.setText(email)
//        tedPhone.setText(phone)
//
//        setGenderRadio(gender)
//    }

//    private fun setGenderRadio(gender: String?) {
//        when (gender) {
//            "Male" -> rdoGroupGender.check(R.id.rdoMale)
//            "Female" -> rdoGroupGender.check(R.id.rdoFemale)
//            "Other" -> rdoGroupGender.check(R.id.rdoOther)
//            else -> rdoGroupGender.clearCheck()
//        }
//    }
//
//    private fun updateProfile() {
//        val email = tedEmail.text.toString().trim()
//        val name = tedName.text.toString().trim()
//        val phone = tedPhone.text.toString().trim().ifEmpty { null }
//        val address = "" // Nếu cần thêm trường địa chỉ
//        val gender = when (rdoGroupGender.checkedRadioButtonId) {
//            R.id.rdoMale -> "Male"
//            R.id.rdoFemale -> "Female"
//            R.id.rdoOther -> "Other"
//            else -> null // Gán null nếu không có lựa chọn
//        }
//
//        // Kiểm tra các trường dữ liệu
//        val validationMessage = validateInputs(email, name, phone, gender)
//        if (validationMessage != null) {
//            Toast.makeText(this, validationMessage, Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val user = ApiUser(
//            userId = email,
//            username = name,
//            email = email,
//            password = "", // Bạn có thể thêm logic để lấy mật khẩu nếu cần
//            phone_number = phone,
//            address = address.takeIf { it.isNotEmpty() }, // Chỉ thêm địa chỉ nếu có giá trị
//            gender = gender
//        )
//
//        apiService.updateProfile(user).enqueue(object : Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (response.isSuccessful) {
//                    Toast.makeText(this@AccountActivity, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show()
//                    saveUserInfo(user)
//                } else {
//                    Toast.makeText(this@AccountActivity, "Cập nhật thất bại: ${response.message()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                Toast.makeText(this@AccountActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    // Hàm kiểm tra đầu vào
//    private fun validateInputs(email: String, name: String, phone: String?, gender: String?): String? {
//        if (email.isEmpty()) {
//            return "Email không được để trống"
//        }
//        if (name.isEmpty()) {
//            return "Tên không được để trống"
//        }
//        if (phone.isNullOrEmpty()) {
//            return "Số điện thoại không được để trống"
//        }
//        if (gender == null) {
//            return "Vui lòng chọn giới tính"
//        }
//        return null // Nếu không có lỗi nào
//    }
//
//    private fun saveUserInfo(user: ApiUser) {
//        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("username", user.username)
//        editor.putString("email", user.email)
//        editor.putString("phone_number", user.phone_number)
//        editor.putString("gender", user.gender ?: "Other")
//        editor.apply()
//
//        Log.d("AccountActivity", "Thông tin người dùng đã được lưu: ${user.username}, Số điện thoại: ${user.phone_number}, Giới tính: ${user.gender}")
//    }
}
