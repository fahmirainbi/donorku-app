package com.example.donorku_app.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.api.model.ResponseModel
import com.example.donorku_app.databinding.ActivityLoginBinding
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.signup.SignUpActivity
import okhttp3.ResponseBody
import org.checkerframework.checker.units.qual.s
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

        binding.pb.visibility = View.VISIBLE


        ApiConfig.instanceRetrofit.loginPost(
            binding.etEmailLogin.text.toString(),
            binding.etPasswordLogin.text.toString(),


            ).enqueue(object : Callback<ResponseModel> {
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {

                binding.pb.visibility = View.GONE
                val respon = response.body()!!

                    val intent = Intent(this@LoginActivity,HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@LoginActivity,
                        "Anda berhasil masuk",
                        Toast.LENGTH_LONG
                    )
                        .show()
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                binding.pb.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Error : " + t.message, Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }


}