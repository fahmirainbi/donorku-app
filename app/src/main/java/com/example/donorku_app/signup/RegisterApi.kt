package com.example.donorku_app.signup


data class RegisterApi(
    val name : String?,
    val email : String?,
    val password : String?,
    val password_confirmation : String?,
    val no_telp : String?,
)