package com.example.donorku_app.api.model


import com.google.gson.annotations.SerializedName

data class PoinDonorPost(
    @SerializedName("poin_donor")
    val poinDonor: Int
)