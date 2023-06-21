package com.example.donorku_app.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivitySignUpBinding
import com.example.donorku_app.login.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnDaftar.setOnClickListener {

            startActivity(Intent(this,LoginActivity::class.java))
            Toast.makeText(this,"Anda berhasil daftar",Toast.LENGTH_LONG).show()
            enableButton()
        }
    }

    fun enableButton() {
        if (binding.etName.text.isEmpty()) {
            binding.etName.error = "Kolom belum diisi"
            binding.etName.requestFocus()
            return
        } else if (binding.etNumber.text.isEmpty()) {
            binding.etNumber.error = "Kolom Belum diisi"
            binding.etNumber.requestFocus()
            return
        } else if (binding.etEmailSignup.text.isEmpty()) {
            binding.etEmailSignup.error = "Kolom Belum diisi"
            binding.etEmailSignup.requestFocus()
            return
        } else if (binding.etPasswordSignup.text.isEmpty()) {
            binding.etPasswordSignup.error = "Kolom Belum diisi"
            binding.etPasswordSignup.requestFocus()
            return
        } else if (binding.etPasswordConfirmation.text.isEmpty()) {
            binding.etPasswordConfirmation.error = "Kolom Belum diisi"
            binding.etPasswordConfirmation.requestFocus()
            return
        }


        ApiConfig.instanceRetrofit.registerPost(
            binding.etName.text.toString(),
            binding.etNumber.text.toString(),
            binding.etEmailSignup.text.toString(),
            binding.etPasswordSignup.text.toString(),
            binding.etPasswordConfirmation.text.toString()

        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })
    }


}