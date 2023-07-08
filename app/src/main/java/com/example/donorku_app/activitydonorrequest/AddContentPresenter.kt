package com.example.donorku_app.activitydonorrequest

import com.example.donorku_app.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddContentPresenter(private val listener: DonorRequestActivity) {
    fun createContent(postContentResponse: Activity){
        ApiConfig.instance.activityRequest(postContentResponse, token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI2IiwianRpIjoiMWM5YmNiZTI5MmE4MzI1ZTA2MGY0OTA5ZTFiNDUwNDcyMmRlYTE4M2NmZmEyOTRhNGUxNTMwY2Q5NzQ5NTYwMmZjYzkzZTlmZjEyY2M5ZjUiLCJpYXQiOjE2ODY2MzgxNDMuMTQwOTQ0MDA0MDU4ODM3ODkwNjI1LCJuYmYiOjE2ODY2MzgxNDMuMTQwOTQ3MTAzNTAwMzY2MjEwOTM3NSwiZXhwIjoxNzE4MjYwNTQzLjEyOTg0ODk1NzA2MTc2NzU3ODEyNSwic3ViIjoiMTIiLCJzY29wZXMiOltdfQ.am0neCGVG5H8YnLbUh2AtClZli5kh_CwKzgOETio8H3GrsP7FE0XPeBdr6ds0yhS0hrViw_33ReFcjjepsdfzFf6J3iQ832AglLt38fKUxGo8ld-MuPI5yGE9OzjGMBPK8KPZoiMrKq-Myrj6RTwfeuPlNAn70ebV-AOLJ4Cl-Mw7Y4zsm_X3M65AOzb1ErLPfuKrU2VZsTZ4C0XJ4uj45LiQEAnJsR_RvxY3v4Jd6jGvT9YrWcZJr2WpKUbinkOr5SLtoi9vA5wKIkKUroTlu1TvKa6lJw2Oq2qAcRJOgXyt1xh8dp7jpI91A7Sm8RYcjLk0bQ3ivPqkFFbBKqV-IAghEOHdKl33yPU2jqbjU1O1nBg5MhEZxR19zlDDOrwcXSMtdZ6VSMmyWbJACCqFtoAFA414K-MsxoL5dVXWiEi_XehDpvrZr8nvNJtmlyOojJIt8-pgjI5FE6I7nktN9RbmW8bNB0OadJFq7k_FVqXAb7wK-SMvgfxEjImus4LgCMlpCfKYmO5x0vtWQjtSVfqOsiDRULBgD0cPPM3BSb-ujJeivFfqBNLWHdeFhgJDGJ5qytDjVu2uxGGh7UIVPQdBORZM2b2b40maNtVWDhJfBDXfq0R9LkfcXME5WrQ4dNB8uXNelfIi5Hm1N042soG4DwPPXAkFkIs3EUAhkE").enqueue(object :Callback<Activity>{
            override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
            listener.onAddContentSuccess("Data Sukses Dikirim")
            }

            override fun onFailure(call: Call<Activity>, t: Throwable) {
                listener.onAddContentFailure("Data Tidak Sesuai")
            }

        })
    }

    interface Listener {
        fun  onAddContentSuccess(sccMessage:String)
        fun  onAddContentFailure(errMessage:String)
    }
}