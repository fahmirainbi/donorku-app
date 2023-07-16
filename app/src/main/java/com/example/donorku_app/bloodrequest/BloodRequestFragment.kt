package com.example.donorku_app.bloodrequest

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.donorku_app.R
import com.example.donorku_app.activitydonorrequest.AddContentPresenter
import com.example.donorku_app.api.model.BloodRequestPost
import com.example.donorku_app.coupondonorku.CouponDonorkuActivity
import com.example.donorku_app.databinding.FragmentBloodRequestBinding
import com.example.donorku_app.faq.FaqActivity
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.home.fragment.HomeFragment
import com.example.donorku_app.home.fragment.menu.EditPasswordActivity
import com.example.donorku_app.home.fragment.menu.EditProfileActivity
import com.google.gson.JsonParser

class BloodRequestFragment : Fragment(), BloodContentPresenter.Listener {
    private lateinit var binding: FragmentBloodRequestBinding
    private lateinit var bloodContentPresenter: BloodContentPresenter

    private var user: String? = null
    private var token: String? = null

    private var userNameTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        user = sharedPreferences.getString("user", null)
        token = sharedPreferences.getString("token", null)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBloodRequestBinding.inflate(inflater, container, false)

        binding.ivBack.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
        binding.btnRegistrationBlood.setOnClickListener {
            setupDataComponent()
            setupListener()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userNameTextView = view?.findViewById(R.id.et_nama)
        if (user != null) {
            val jsonObject = JsonParser.parseString(user).asJsonObject
            userNameTextView?.setText(jsonObject.get("name").asString)
        }
    }

    override fun onResume() {
        super.onResume()
        val blood = resources.getStringArray(R.array.blood)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_blood, blood)
        binding.autoCompleteTextView2.setAdapter(arrayAdapter)
    }

    private fun setupDataComponent() {
        bloodContentPresenter = BloodContentPresenter(requireContext(),this@BloodRequestFragment)
    }

    override fun onAddContentSuccess(sccMessage: String) {

    }

    override fun onAddContentFailure(errMessage: String) {
    }

    private fun setupListener() {
            val kebutuhan = binding.etRequest.text.toString().trim()
            val golongan_darah = binding.autoCompleteTextView2.text.toString().trim()
            val nomor = binding.etNumber.text.toString().trim()
            val deskripsi = binding.etDescription.text.toString().trim()

            when {
                (kebutuhan.isEmpty()) -> {
                    binding.etRequest.error = "Kolom Kosong"
                    binding.etRequest.requestFocus()
                }
                (golongan_darah.isEmpty()) -> {
                    binding.autoCompleteTextView2.error = "Kolom Kosong"
                    binding.autoCompleteTextView2.requestFocus()
                }
                (nomor.isEmpty()) -> {
                    binding.etNumber.error = "Kolom Kosong"
                    binding.etNumber.requestFocus()
                }
                (deskripsi.isEmpty()) -> {
                    binding.etDescription.error = "Kolom Kosong"
                    binding.etDescription.requestFocus()
                }else ->{
                val jsonObject = JsonParser.parseString(user).asJsonObject
                val userId = jsonObject.get("id").asInt
                val posContent = BloodRequestPost(userId, kebutuhan, golongan_darah, nomor, deskripsi)
                bloodContentPresenter.makeContent(posContent)

                allertDialogSucces()
                }


            }
    }

    fun allertDialogSucces() {
        val dialogBinding = layoutInflater.inflate(R.layout.alert_dialog_custom_, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogBinding)

        val alertDialog = builder.create()

        val judul = dialogBinding.findViewById<TextView>(R.id.tvHead)
        val gambar = dialogBinding.findViewById<ImageView>(R.id.ivdialog)
        val deskripsi = dialogBinding.findViewById<TextView>(R.id.tvdeskripsi)
        val buton = dialogBinding.findViewById<Button>(R.id.btnAlert)

        judul.setText("Pengajuan Permintaan darah anda Berhasil")
        gambar.setImageResource(R.drawable.img_success)
        deskripsi.setText("Pengajuan permintaan darah berhasil silahkan menunggu 1X24 Jam, petugas kami akan menghubungi secepatnya")

        buton.setOnClickListener {
            requireActivity().run {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


}

