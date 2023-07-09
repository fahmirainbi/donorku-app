package com.example.donorku_app.home.fragment.menu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.donorku_app.R
import com.example.donorku_app.bloodrequest.BloodRequestActivity
import com.example.donorku_app.databinding.FragmentHomeBinding
import com.example.donorku_app.databinding.FragmentMenuBinding
import com.example.donorku_app.faq.FaqActivity
import com.example.donorku_app.login.LoginActivity
import com.google.gson.JsonParser

class MenuFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentMenuBinding
    private var user: String? = null
    private var token: String? = null

    private lateinit var logoutBtn: LinearLayout
    private lateinit var btnPassword: LinearLayout
    private lateinit var editProfileBtn: ImageView
    private var userNameTextView: TextView? = null
    private var userPhoneTextView: TextView? = null
    private var userPointTextView: TextView? = null

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

        binding.btnFaq.setOnClickListener {
            requireActivity().run {
                val intent =Intent(this, FaqActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }



        userNameTextView = view?.findViewById(R.id.userNameTextViewInMenu)
        userPhoneTextView = view?.findViewById(R.id.userPhoneTextViewInMenu)
        userPointTextView = view?.findViewById(R.id.userPointTextView)
        if (user != null) {
            val jsonObject = JsonParser.parseString(user).asJsonObject
            userNameTextView?.setText(jsonObject.get("name").asString)
            userPhoneTextView?.setText(jsonObject.get("no_telp").asString)
            userPointTextView?.setText(jsonObject.get("poin_donor").asString)
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
}