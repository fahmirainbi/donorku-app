package com.example.donorku_app.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.ResponseModel
import com.example.donorku_app.databinding.ActivitySignUpBinding
import com.example.donorku_app.home.HomeActivity
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
        } else if (binding.etPasswordConfirmation.text.isEmpty() && binding.etPasswordConfirmation.text != binding.etPasswordSignup.text) {
            binding.etPasswordConfirmation.error = "Kolom Belum diisi / Password tidak sesuai"
            binding.etPasswordConfirmation.requestFocus()
            return
        }


        ApiConfig.instanceRetrofit.registerPost(
            binding.etName.text.toString(),
            binding.etNumber.text.toString(),
            binding.etEmailSignup.text.toString(),
            binding.etPasswordSignup.text.toString(),
            binding.etPasswordConfirmation.text.toString()

        ).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {


                val respon = response.body()!!

                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Toast.makeText(
                    this@SignUpActivity,
                    "Anda berhasil daftar",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Error : " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }


}