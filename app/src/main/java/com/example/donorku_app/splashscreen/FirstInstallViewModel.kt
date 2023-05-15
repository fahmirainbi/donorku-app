package com.example.donorku_app.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class FirstInstallViewModel(application: Application): AndroidViewModel(application) {

    fun createFirstInstallData(name: String): FirstInstall {
        return FirstInstall(name, getApplication())
    }

    fun getBoolean(data: FirstInstall, defaultValue: Boolean): Boolean {
        return data.getBoolean("STATE", defaultValue)
    }
    // Method akan digunakan untuk menyimpan nama player dan item
    fun saveBoolean(data: FirstInstall, value: Boolean) {
        data.putBoolean("STATE", value)
    }

}