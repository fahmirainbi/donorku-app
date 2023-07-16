package com.example.donorku_app.home.fragment.menu.Transaksi

import android.content.Context
import android.content.SharedPreferences
import android.util.JsonToken
import android.util.Log
import android.widget.Toast
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.BloodRequestPost
import com.example.donorku_app.api.model.ChangePointPost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetPoinPresenter(private val context: Context,private val listener:Listener) {
    private var token: String? = null
    fun makeContent(postContentResponse: ChangePointPost){
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("session", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)
        ApiConfig.instanceRetrofit.postKuppon(postContentResponse, "Bearer " + token).enqueue(object :
            Callback<ChangePointPost> {
            override fun onResponse(
                call: Call<ChangePointPost>,
                response: Response<ChangePointPost>
            ) {
                if (response.isSuccessful) {
                    listener.onAddContentSuccess("Data Sukses Dikirim")
                } else {
                    listener.onAddContentFailure("Gagal mengirim data")
                }
            }

            override fun onFailure(call: Call<ChangePointPost>, t: Throwable) {
                listener.onAddContentFailure("Data Tidak Sesuai" +t.message)
                Log.d("Data","Data Tidak sesuai" +t.message)
            }

        })
    }

    interface Listener {
        fun  onAddContentSuccess(sccMessage:String)
        fun  onAddContentFailure(errMessage:String)
    }
}