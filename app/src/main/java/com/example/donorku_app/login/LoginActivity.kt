package com.example.donorku_app.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityLoginBinding
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.signup.SignUpActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            enableButton()
            Toast.makeText(this,"Anda berhasil masuk",Toast.LENGTH_LONG).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.ivSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    fun enableButton() {
        if (binding.etEmailLogin.text.isEmpty()) {
            binding.etEmailLogin.error = "Kolom belum diisi"
            binding.etEmailLogin.requestFocus()
            return
        } else if (binding.etPasswordLogin.text.isEmpty()) {
            binding.etPasswordLogin.error = "Kolom Belum diisi"
            binding.etPasswordLogin.requestFocus()
            return
        }


        ApiConfig.instanceRetrofit.loginPost(
            binding.etEmailLogin.text.toString(),
            binding.etPasswordLogin.text.toString(),


            ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error : " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }


}