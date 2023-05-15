package com.example.donorku_app.splashscreen

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.example.donorku_app.R
import com.example.donorku_app.databinding.ActivitySplashScreenBinding
import com.example.donorku_app.landingpage.LandingPageActivity
import com.example.donorku_app.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var  mFirstInstallViewModel: FirstInstallViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mFirstInstallViewModel = ViewModelProvider(this)[FirstInstallViewModel::class.java]

        val firstInstallData = mFirstInstallViewModel.createFirstInstallData("FIRST_INSTALL")

        if (mFirstInstallViewModel.getBoolean(firstInstallData, true)) {
            mFirstInstallViewModel.saveBoolean(firstInstallData, false)
            Handler().postDelayed({
                val intent = Intent(this@SplashScreenActivity, LandingPageActivity::class.java)
                startActivity(intent)
                finish()
            }, 5000)
        } else {
            Handler().postDelayed({
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 5000)
        }
    }
}