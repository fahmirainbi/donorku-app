package com.example.donorku_app.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivityLoginBinding
import com.example.donorku_app.home.HomeActivity
import com.example.donorku_app.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
        val user    = sharedPreferences.getString("user", null)
        val token   = sharedPreferences.getString("token", null)

        if (user != null && token != null) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish();

        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()



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
        auth.signInWithEmailAndPassword(
            binding.etEmailLogin.text.toString(),
            binding.etPasswordLogin.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user?.isEmailVerified == true) {
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Anda harus memverifikasi email Anda terlebih dahulu.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
            }
        }
        binding.pb.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.loginPost(
            binding.etEmailLogin.text.toString(),
            binding.etPasswordLogin.text.toString()
        ).enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                val json = response.body()?.asJsonObject
                if(json?.has("error_message") == true){
                    Toast.makeText(this@LoginActivity, json?.get("error_message")?.asString, Toast.LENGTH_SHORT)
                        .show()
                } else{
                    val user = json?.get("user").toString()
                    val token = json?.get("token")?.asString
                    val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE)
                    val editorSharedPreferences = sharedPreferences.edit()
                    editorSharedPreferences.putString("user", user)
                    editorSharedPreferences.putString("token", token)
                    editorSharedPreferences.apply()
                    Toast.makeText(this@LoginActivity,"Anda berhasil masuk",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish();
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error : " + t.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }


}