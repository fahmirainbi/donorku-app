package com.example.donorku_app.api.model


import com.google.gson.annotations.SerializedName

data class BloodRequestPost(
    @SerializedName("user")
    val id : Int,
    @SerializedName("kebutuhan_darah")
    val kebutuhanDarah: String,
    @SerializedName("tipe_darah")
    val tipeDarah: String,
    @SerializedName("no_telp_pasien_keluarga")
    val noTelpPasienKeluarga: String,
    @SerializedName("catatan")
    val catatan: String,
)