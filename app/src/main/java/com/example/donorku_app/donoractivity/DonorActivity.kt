package com.example.donorku_app.donoractivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donorku_app.databinding.ActivityDonorBinding
import com.example.donorku_app.home.HomeActivity


class DonorActivity: AppCompatActivity(),MainPresenter.Listener {
    private lateinit var binding: ActivityDonorBinding
    private lateinit var donorAdapter: DonorAdapter
    private lateinit var mainPresenter: MainPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        setUpComponent()





        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        mainPresenter.getDonor()
    }

    private fun setUpComponent(){
        mainPresenter = MainPresenter(this)
        donorAdapter = DonorAdapter()
        binding.rvDonor.setHasFixedSize(true)
        binding.rvDonor.layoutManager= LinearLayoutManager(this)
        binding.rvDonor.setAdapter(donorAdapter)

    }

    override fun onJadwalGetSuccess(contentList: MutableList<Donor>) {
    donorAdapter.addContentList(contentList)
        Toast.makeText(this,"Data Success",Toast.LENGTH_SHORT).show()
    }

    override fun onJadwalGetFailure(errMessage: String) {
    Toast.makeText(this,errMessage,Toast.LENGTH_SHORT).show()
    }


}