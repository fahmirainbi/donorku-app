package com.example.donorku_app.home.fragment.menu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.donorku_app.R
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.FragmentMenuBinding
import com.example.donorku_app.faq.FaqActivity
import com.example.donorku_app.home.fragment.menu.Transaksi.ChangePointActivity
import com.example.donorku_app.login.LoginActivity
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private var user: String? = null
    private lateinit var userId: JsonObject
    private var token: String? = null

    private lateinit var logoutBtn: LinearLayout
    private lateinit var btnPassword: LinearLayout
    private lateinit var editProfileBtn: ImageView
    private var userNameTextView: TextView? = null
    private var userPhoneTextView: TextView? = null
    private var userPointTextView: TextView? = null
    private var listData: ArrayList<JsonObject> = arrayListOf()

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        userId = JsonParser.parseString(sharedPreferences.getString("user", null)).asJsonObject
        token = sharedPreferences.getString("token", null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoutBtn = view.findViewById(R.id.logout_btn)
        logoutBtn?.setOnClickListener {
            logout()
        }

        btnPassword = view.findViewById(R.id.btn_password)
        btnPassword?.setOnClickListener {
            startActivity(Intent(requireContext(), EditPasswordActivity::class.java))
        }

        editProfileBtn = view.findViewById(R.id.btn_edit_profile)
        editProfileBtn?.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }

        binding.linkWa.movementMethod = LinkMovementMethod.getInstance()

        binding.btnPoin.setOnClickListener {
            requireActivity().run {
                val intent = Intent(this, ChangePointActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        binding.btnFaq.setOnClickListener {
            requireActivity().run {
                val intent = Intent(this, FaqActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }



        userNameTextView = view?.findViewById(R.id.userNameTextViewInMenu)
        userPhoneTextView = view?.findViewById(R.id.userPhoneTextViewInMenu)
        userPointTextView = view?.findViewById(R.id.userPointTextView)
        if (user != null) {
            handler.post(updatePointsRunnable)
            val jsonObject = JsonParser.parseString(user).asJsonObject
            userNameTextView?.setText(jsonObject.get("name").asString)
            userPhoneTextView?.setText(jsonObject.get("no_telp").asString)

        }
    }

    private val updatePointsRunnable = object : Runnable {
        override fun run() {
            getData()

            handler.postDelayed(this, updateInterval)
        }
    }

    private fun logout() {
        val sharedPreferences: SharedPreferences =
            requireContext().applicationContext.getSharedPreferences(
                "session",
                Context.MODE_PRIVATE
            )
        val editorSharedPreferences = sharedPreferences.edit()
        editorSharedPreferences.clear()
        editorSharedPreferences.apply()
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finish();
    }


    private fun getData() {
        listData.clear()
        ApiConfig.instanceRetrofit.getPoin(
            "Bearer " + token,
            userId.get("id")?.asInt
        ).enqueue(object : Callback<JsonElement> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val dataObject = responseBody.asJsonObject
                        val poinDonor = dataObject.get("data").asJsonObject?.get("poin_donor")?.asInt
                        userPointTextView?.text = poinDonor.toString()


                        val currentDate = LocalDate.now()
                        val currentYear = currentDate.year
                        val expirationDate = LocalDate.of(currentYear, 12, 31)
                        val dateFormatter =
                            DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
                        val formattedExpirationDate = expirationDate.format(dateFormatter)


                        if (currentDate.isAfter(expirationDate)) {
                            val textPoin = 0
                            userPointTextView?.text = textPoin.toString()
                            ApiConfig.instanceRetrofit.minPoin(
                                "Bearer " + token,
                                userId.get("id")?.asInt,
                                poinDonor
                            ).enqueue(object : Callback<JsonElement> {
                                override fun onResponse(
                                    call: Call<JsonElement>,
                                    response: Response<JsonElement>
                                ) {

                                }

                                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                                    Log.d("Data", "Data Tidak sesuai" + t.message)
                                }

                            })
                        }


                        if (poinDonor != 0) {
                            binding.tvDateExpPoin.visibility = android.view.View.VISIBLE
                            binding.tvDescExpPoin.visibility = android.view.View.VISIBLE
                            binding.tvDateExpPoin.text = formattedExpirationDate.toString()
                        } else {
                            binding.tvDateExpPoin.visibility = android.view.View.GONE
                            binding.tvDescExpPoin.text = ""
//                        binding.tvDescExpPoin.setTextColor(resources.getColor(R.color.green))
                        }
                        userPointTextView?.text = poinDonor.toString()
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error : " + t.message.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        })
    }
}