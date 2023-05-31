package com.example.donorku_app.bloodrequest

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.donorku_app.databinding.ActivityBloodRequestBinding
import com.example.donorku_app.home.HomeActivity


class BloodRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodRequestBinding

    private var mFragmentTransaction: FragmentTransaction? = null
    private var mFragmentManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBloodRequestBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        mFragmentManager = supportFragmentManager
        mFragmentTransaction = mFragmentManager!!.beginTransaction()
        mFragmentTransaction!!.replace(com.example.donorku_app.R.id.container, BloodRequestFragment())
        mFragmentTransaction!!.commit()


    }
}