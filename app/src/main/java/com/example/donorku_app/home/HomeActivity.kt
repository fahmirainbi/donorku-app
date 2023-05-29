package com.example.donorku_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.donorku_app.R
import com.example.donorku_app.databinding.ActivityHomeBinding
import com.example.donorku_app.home.fragment.HistoryFragment
import com.example.donorku_app.home.fragment.HomeFragment
import com.example.donorku_app.home.fragment.MenuFragment
import com.example.donorku_app.home.fragment.RegistrationFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




//      Bottom Nav
        replaceFragment(HomeFragment())
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeFragment ->replaceFragment(HomeFragment())
                R.id.registrationFragment ->replaceFragment(RegistrationFragment())
                R.id.historyFragment ->replaceFragment(HistoryFragment())
                R.id.menuFragment ->replaceFragment(MenuFragment())

                else -> {

                }
            }
            true
        }


    }

    private fun  replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper,fragment)
        fragmentTransaction.commit()
    }

    private fun makeCurrentFragment(fragment: Fragment) =

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}