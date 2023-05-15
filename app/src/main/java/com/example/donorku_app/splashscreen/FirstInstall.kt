package com.example.donorku_app.splashscreen

import android.content.Context

class FirstInstall (name:String, context: Context) {
    private val sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    // Method akan digunakan untuk menyimpan nama player dan item
    fun putBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }
}