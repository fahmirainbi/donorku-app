package com.example.donorku_app.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.donorku_app.R
import com.example.donorku_app.databinding.ActivityHomeBinding
import com.example.donorku_app.home.fragment.HistoryFragment
import com.example.donorku_app.home.fragment.HomeFragment
import com.example.donorku_app.home.fragment.MenuFragment
import com.example.donorku_app.home.fragment.RegistrationFragment
import com.example.donorku_app.home.imageslider.ImageAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Math.abs

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter


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