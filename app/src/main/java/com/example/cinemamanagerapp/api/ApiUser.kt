package com.example.cinemamanagerapp.api

class ApiUser {
    var userId: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var phone_number: String? = null
    var address: String? = null
    var gender: String? = null

    // Constructor, getters, setters, và các phương thức khác
    constructor(
        userId: String,
        username: String,
        email: String,
        password: String,
        phone_number: String?,
        address: String?,
        gender: String?
    ) {
        this.userId = userId
        this.username = username
        this.email = email
        this.password = password
        this.phone_number = phone_number
        this.address = address
        this.gender = gender
    }

}
