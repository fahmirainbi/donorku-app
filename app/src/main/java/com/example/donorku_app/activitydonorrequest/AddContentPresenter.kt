package com.example.donorku_app.activitydonorrequest

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.ActivityRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddContentPresenter(
    private val context: Context,
    private val listener: DonorRequestActivity
) {
    private var token: String? = null
    fun createContent(postContentResponse: ActivityRequest) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("session", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)
        ApiConfig.instanceRetrofit.activityRequest(
            postContentResponse,
            "Bearer " + token
        ).enqueue(object : Callback<ActivityRequest> {
            override fun onResponse(
                call: Call<ActivityRequest>,
                response: Response<ActivityRequest>
            ) {
                listener.onAddContentSuccess("Data Sukses Dikirim")
                Log.d("Data", "sukser")
            }

            override fun onFailure(call: Call<ActivityRequest>, t: Throwable) {
                listener.onAddContentFailure("Data Tidak Sesuai")
                Log.d("Data", "message" + t.message)
            }

        })
    }

    interface Listener {
        fun onAddContentSuccess(sccMessage: String)
        fun onAddContentFailure(errMessage: String)
    }
}