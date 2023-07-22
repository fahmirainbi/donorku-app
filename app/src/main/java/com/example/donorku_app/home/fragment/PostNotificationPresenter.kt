package com.example.donorku_app.home.fragment

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.donorku_app.activitydonorrequest.DonorRequestActivity
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.ActivityRequest
import com.example.donorku_app.api.model.PostNotifikasiUpdatePoin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostNotificationPresenter(
    private val context: Context,
    private val listener: BottomSheetHistoryFragment
) {
    private var token: String? = null

    fun createContent(postContentResponse: PostNotifikasiUpdatePoin) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("session", Context.MODE_PRIVATE)
        token = sharedPreferences.getString("token", null)
        ApiConfig.instanceRetrofit.notification(
            postContentResponse,
            "Bearer " + token
        ).enqueue(object : Callback<PostNotifikasiUpdatePoin> {
            override fun onResponse(
                call: Call<PostNotifikasiUpdatePoin>,
                response: Response<PostNotifikasiUpdatePoin>
            ) {
//                listener.onAddContentSuccess("Data Sukses Dikirim")
                Log.d("Data", "sukser")
            }

            override fun onFailure(call: Call<PostNotifikasiUpdatePoin>, t: Throwable) {
//                listener.onAddContentFailure("Data Tidak Sesuai")
                Log.d("Data", "message" + t.message)
            }

        })
    }

    interface Listener {
        fun onAddContentSuccess(sccMessage: String)
        fun onAddContentFailure(errMessage: String)
    }
}