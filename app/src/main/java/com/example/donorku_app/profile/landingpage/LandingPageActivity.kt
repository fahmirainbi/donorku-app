package com.example.donorku_app.profile.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.donorku_app.R
import com.example.donorku_app.databinding.ActivityLandingPageBinding

class LandingPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLandingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val text1 = "Selamat datang di aplikasi Donor Darah, tempat di mana Anda bisa menyelamatkan nyawa dengan satu tindakan sederhana."
        val text2 = "Jadilah pahlawan dalam hidup seseorang. Donorkan darah Anda sekarang dan berikan harapan baru bagi mereka yang membutuhkannya."
        val text3 = "Setetes darah Anda bisa menjadi pemberi harapan. Bergabunglah dengan komunitas donor darah kami dan berikan kehidupan kepada yang membutuhkan."
        val fragmentOne =
            LandingPageFragment.newInstance(R.drawable.image_landing_page1, text1, View.INVISIBLE)
        val fragmentTwo =
            LandingPageFragment.newInstance(R.drawable.image_landing_page2, text2, View.VISIBLE)
        val fragmentThree =
            LandingPageFragment.newInstance(R.drawable.image_landing_page3, text3, View.VISIBLE)
        //val fragmentThree = LandingPageFragment.newInstance(R.drawable.landingpage3, teks3)

        val listFragment = listOf(fragmentOne,fragmentTwo,fragmentThree)
        val adapter = LandingPageAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
            data = listFragment
        )

        binding.vpLandingpage.adapter = adapter
    }
}