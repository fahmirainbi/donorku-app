package com.example.donorku_app.bloodrequest

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.BloodRequestPost
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BloodContentPresenter(private val context: Context,private val listener: Listener) {
    private var token: String? = null

    fun makeContent(postContentResponse: BloodRequestPost) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("session", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)
        ApiConfig.instanceRetrofit.donorRequest(
            postContentResponse,
            "Bearer " + token).enqueue(object :
            Callback<BloodRequestPost> {
            override fun onResponse(
                call: Call<BloodRequestPost>,
                response: Response<BloodRequestPost>
            ) {
                listener.onAddContentSuccess("Data Sukses Dikirim")
            }

            override fun onFailure(call: Call<BloodRequestPost>, t: Throwable) {
                listener.onAddContentFailure("Data Tidak Sesuai")
                Log.d("Data", "Data :" + t.message)
            }

        })
    }

//    fun editPoint(){
//        ApiConfig.instanceRetrofit.
//    }

    interface Listener {
        fun onAddContentSuccess(sccMessage: String)
        fun onAddContentFailure(errMessage: String)
    }
}