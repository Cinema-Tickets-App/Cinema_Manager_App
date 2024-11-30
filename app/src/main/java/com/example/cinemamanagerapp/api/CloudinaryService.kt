package com.example.cinemamanagerapp.api

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CloudinaryService {

    private val cloudinary: Cloudinary = Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", "dcgkbexah",
            "api_key", "231589156413695",
            "api_secret", "wvmRE2PzwHnpF6XkJer-IORuRKQ"
        )
    )

    fun uploadImage(file: File, callback: (String?) -> Unit) {
        val requestFile = RequestBody.create("image/*".toMediaType(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Tải lên ảnh và nhận URL
        try {
            val uploadResult =
                cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "avatars"))
            val secureUrl = uploadResult["secure_url"] as? String
            callback(secureUrl)
        } catch (e: Exception) {
            callback(null)
        }
    }
}



