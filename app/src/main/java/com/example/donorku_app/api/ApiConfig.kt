package com.example.donorku_app.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "https://pmi-fahmi.agyson.com/api/"
    private val client: Retrofit
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interseptor = HttpLoggingInterceptor()
            interseptor.level = HttpLoggingInterceptor.Level.BODY
            val client : OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interseptor)
                .connectTimeout(40,TimeUnit.SECONDS)
                .readTimeout(40,TimeUnit.SECONDS)
                .writeTimeout(40,TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }
    val instanceRetrofit : Api
    get() = client.create(Api::class.java)
}