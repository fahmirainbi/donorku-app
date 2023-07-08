package com.example.donorku_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.donorku_app.R
import com.example.donorku_app.databinding.ActivityHomeBinding
import com.example.donorku_app.home.fragment.HistoryFragment
import com.example.donorku_app.home.fragment.HomeFragment
import com.example.donorku_app.home.fragment.menu.MenuFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeFragment = HomeFragment()
    private val historyFragment = HistoryFragment()
    private val menuFragment = MenuFragment()


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