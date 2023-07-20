package com.example.donorku_app.api.model


import com.google.gson.annotations.SerializedName

data class RegistrationPost(
    @SerializedName("questioner_pendonor")
    val questionerPendonor: String,
    @SerializedName("status_donor")
    val statusDonor: String,
    @SerializedName("user_id")
    val user: Int
)