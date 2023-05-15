package com.example.donorku_app.landingpage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class LandingPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val data : List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return data.size
    }

    override fun createFragment(position: Int): Fragment {
        return data[position]
    }
}