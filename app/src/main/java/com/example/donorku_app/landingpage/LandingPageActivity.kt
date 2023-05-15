package com.example.donorku_app.landingpage

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

        val text1 = "Bermain Suit VS Sesama Player"
        val text2 = "Bermain Suit VS Computer"
        val text3 = "teks 3"
        val fragmentOne = LandingPageFragment.newInstance(R.drawable.image_landing_page1, text1, View.INVISIBLE)
        val fragmentTwo = LandingPageFragment.newInstance(R.drawable.image_landing_page2, text2, View.VISIBLE)
        val fragmentThree = LandingPageFragment.newInstance(R.drawable.image_landing_page3, text3, View.VISIBLE)
        //val fragmentThree = LandingPageFragment.newInstance(R.drawable.landingpage3, teks3)

        val listFragment = listOf(fragmentOne,fragmentTwo)
        val adapter = LandingPageAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
            data = listFragment
        )

        binding.vpLandingpage.adapter = adapter
    }
}