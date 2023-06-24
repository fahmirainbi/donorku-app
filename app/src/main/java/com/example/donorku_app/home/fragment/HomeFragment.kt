package com.example.donorku_app.home.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.donorku_app.R
import com.example.donorku_app.activitydonorrequest.DonorRequestActivity
import com.example.donorku_app.bloodrequest.BloodRequestActivity
import com.example.donorku_app.coupondonorku.CouponDonorkuActivity
import com.example.donorku_app.databinding.FragmentHomeBinding
import com.example.donorku_app.donoractivity.DonorActivity
import com.example.donorku_app.donoremergency.DonorEmergencyActivity
import com.example.donorku_app.home.imageslider.ImageData
import com.example.donorku_app.home.imageslider.ImageSliderAdapter
import com.example.donorku_app.notification.NotificationActivity
import com.example.donorku_app.stockdonor.StockBloodActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ImageSliderAdapter
    private val list = ArrayList<ImageData>()

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable


    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var index = 0
            override fun run() {
                if (index == list.size)
                    index = 0
                Log.e("Runnable", "$index")
                binding.viewPager2.setCurrentItem(index)
                index++
                handler.postDelayed(this, 4000)
            }
        }

//        Image Slider
        list.add(
            ImageData(
                R.drawable.image_slider1
            )
        )

        list.add(
            ImageData(
                R.drawable.image_slider2
            )
        )

        list.add(
            ImageData(
                R.drawable.image_slider3
            )
        )

        adapter = ImageSliderAdapter(list)
        binding.viewPager2.adapter = adapter



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding button
        binding.btnBlood.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,BloodRequestActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnBloodStock.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,StockBloodActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnCoupon.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,CouponDonorkuActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()

            }
        }

        binding.btnInfo.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,DonorEmergencyActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnDonorActivity.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,DonorActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnReqActivity.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,DonorRequestActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.icNotification.setOnClickListener{
            requireActivity().run {
                val intent =Intent(this,NotificationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }


    }

//  Image slider
    override fun onStart() {
        super.onStart()
        handler.post(runnable)

    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}