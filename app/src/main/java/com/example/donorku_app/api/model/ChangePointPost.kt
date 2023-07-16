package com.example.donorku_app.api.model


import com.google.gson.annotations.SerializedName

data class ChangePointPost(
    @SerializedName("user")
    val id: Int,
    @SerializedName("barang")
    val barang: Int,
    @SerializedName("kode_kupon")
    val kodeKupon: String?,
    @SerializedName("batas_penukaran")
    val batasPenukaran: String?,
    @SerializedName("status_penukaran")
    val statusPenukaran: String?,
)