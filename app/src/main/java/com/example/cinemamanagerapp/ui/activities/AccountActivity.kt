package com.example.cinemamanagerapp.ui.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cinemamanagerapp.R
import com.example.cinemamanagerapp.api.ApiService
import com.example.cinemamanagerapp.api.CloudinaryService
import com.example.cinemamanagerapp.api.RetrofitClient
import com.example.cinemamanagerapp.api.UserProfileUpdateRequest
import com.example.cinemamanagerapp.api.UserProfileResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AccountActivity : AppCompatActivity() {

    private lateinit var imgUserAccount: ImageView
    private lateinit var btnBackAccount: ImageButton
    private lateinit var btnSaveAccount: Button
    private lateinit var tilEmailAccount: TextInputLayout
    private lateinit var tilNameAccount: TextInputLayout
    private lateinit var tilPhoneAccount: TextInputLayout
    private lateinit var tilAddress: TextInputLayout
    private lateinit var tilAgeAccount: TextInputLayout
    private lateinit var rdoGroupGender: RadioGroup
    private lateinit var tedEmailAccount: TextInputEditText
    private lateinit var tedNameAccount: TextInputEditText
    private lateinit var tedPhoneAccount: TextInputEditText
    private lateinit var tedAddress: TextInputEditText
    private lateinit var tedAgeAccount: TextInputEditText

    private lateinit var apiService: ApiService
    private var selectedImageUri: Uri? = null

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
        tilAgeAccount = findViewById(R.id.tilAgeAccount)
        rdoGroupGender = findViewById(R.id.rdoGroupGender)
        tedEmailAccount = findViewById(R.id.tedEmailAccount)
        tedNameAccount = findViewById(R.id.tedNameAccount)
        tedPhoneAccount = findViewById(R.id.tedPhoneAccount)
        tedAddress = findViewById(R.id.tedAddress)
        tedAgeAccount = findViewById(R.id.tedAgeAccount)

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

        // Sự kiện khi nhấn ảnh đại diện
        imgUserAccount.setOnClickListener {
            selectImage()
        }
    }

    private fun getUserProfile() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        if (userId != -1) {
            apiService.getProfile(userId).enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { userProfile ->
                            displayUserProfile(userProfile)
                        }
                    } else {
                        Toast.makeText(this@AccountActivity, "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Toast.makeText(this@AccountActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "ID người dùng không hợp lệ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayUserProfile(userProfile: UserProfileResponse) {
        tedEmailAccount.setText(userProfile.email ?: "")
        tedNameAccount.setText(userProfile.full_name ?: "")
        tedPhoneAccount.setText(userProfile.phone_number ?: "")
        tedAddress.setText(userProfile.address ?: "")
        tedAgeAccount.setText(userProfile.age?.toString() ?: "")

        when (userProfile.gender) {
            "Male" -> rdoGroupGender.check(R.id.rdoMale)
            "Female" -> rdoGroupGender.check(R.id.rdoFemale)
            else -> rdoGroupGender.check(R.id.rdoOther)
        }

        userProfile.avatar_url?.let {
            Glide.with(this@AccountActivity)
                .load(it)
                .placeholder(R.drawable.icon_avatar)
                .error(R.drawable.ic_launcher_foreground)
                .into(imgUserAccount)
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            imgUserAccount.setImageURI(selectedImageUri)
        }
    }

    private fun updateUserProfile() {
        // Lấy user_id từ SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        // Kiểm tra nếu user_id không hợp lệ
        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy user_id", Toast.LENGTH_SHORT).show()
            Log.e("UserProfile", "Error: Invalid userId, not found in SharedPreferences")
            return
        }

        // Lấy dữ liệu từ các trường nhập liệu
        val email = tedEmailAccount.text.toString()
        val fullName = tedNameAccount.text.toString()
        val phone = tedPhoneAccount.text.toString()
        val address = tedAddress.text.toString()
        val ageText = tedAgeAccount.text.toString()

        // Kiểm tra các trường không được để trống
        if (email.isEmpty()) {
            tedEmailAccount.error = "Email không được để trống"
            return
        }

        if (fullName.isEmpty()) {
            tedNameAccount.error = "Tên không được để trống"
            return
        }

        if (phone.isEmpty()) {
            tedPhoneAccount.error = "Số điện thoại không được để trống"
            return
        }

        if (address.isEmpty()) {
            tedAddress.error = "Địa chỉ không được để trống"
            return
        }

        // Kiểm tra định dạng email hợp lệ
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tedEmailAccount.error = "Email không hợp lệ"
            return
        }

        // Kiểm tra số điện thoại hợp lệ (tối thiểu 10 ký tự)
        if (phone.length < 10) {
            tedPhoneAccount.error = "Số điện thoại phải có ít nhất 10 ký tự"
            return
        }

        // Kiểm tra tuổi hợp lệ
        val age = ageText.toIntOrNull()
        if (age == null || age <= 0 || age < 18 || age > 100) {
            tedAgeAccount.error = "Tuổi phải là một giá trị hợp lệ và từ 18 đến 100"
            return
        }

        // Kiểm tra địa chỉ không chứa ký tự đặc biệt
        val addressPattern = "^[a-zA-Z0-9\\s,]+$"
        if (!address.matches(Regex(addressPattern))) {
            tedAddress.error = "Địa chỉ không được chứa ký tự đặc biệt"
            return
        }

        // Kiểm tra tên không chứa số
        if (fullName.contains(Regex("[0-9]"))) {
            tedNameAccount.error = "Tên không được chứa số"
            return
        }

//        // Kiểm tra ảnh đại diện (nếu có) phải là một URI hợp lệ
//        if (selectedImageUri != null) {
//            val file = File(getRealPathFromURI(selectedImageUri!!))
//            if (!file.exists()) {
//                Toast.makeText(this, "Ảnh đại diện không hợp lệ", Toast.LENGTH_SHORT).show()
//                return
//            }
//        }

        val gender = when (rdoGroupGender.checkedRadioButtonId) {
            R.id.rdoMale -> "Male"
            R.id.rdoFemale -> "Female"
            else -> "Other"
        }

        // Tạo đối tượng UserProfileUpdateRequest
        val updatedProfile = UserProfileUpdateRequest(
            email = email,
            full_name = fullName,
            phone_number = phone,
            address = address,
            age = age,
            gender = gender,
            avatar_url = ""  // Có thể để trống nếu không có ảnh
        )

        // Gọi API để cập nhật thông tin người dùng
        apiService.updateProfile(userId, updatedProfile).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AccountActivity, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AccountActivity, "Không thể cập nhật thông tin", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AccountActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun uploadImageToCloudinary(uri: Uri): String {
        val file = File(getRealPathFromURI(uri))
        // Giả sử bạn có một service để tải ảnh lên Cloudinary
        val cloudinaryService = CloudinaryService()
        var avatarUrl = ""

        cloudinaryService.uploadImage(file) { url ->
            avatarUrl = url ?: ""
        }

        return avatarUrl
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)

            // Kiểm tra xem columnIndex có hợp lệ không
            if (columnIndex != -1) {
                it.moveToFirst()
                val path = it.getString(columnIndex)
                it.close()
                return path
            } else {
                it.close()
                return "" // Hoặc thông báo lỗi nếu không tìm thấy cột
            }
        }
        return "" // Trả về chuỗi rỗng nếu không tìm thấy con trỏ
    }

}
