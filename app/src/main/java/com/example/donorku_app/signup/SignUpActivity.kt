package com.example.donorku_app.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.donorku_app.api.ApiConfig
import com.example.donorku_app.databinding.ActivitySignUpBinding
import com.example.donorku_app.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null && auth.currentUser?.isEmailVerified == true) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnDaftar.setOnClickListener {
            authentification()
            enableButton()
            Toast.makeText(this@SignUpActivity, "Anda berhasil daftar", Toast.LENGTH_SHORT)
                .show()

            binding.etName.text.clear()
            binding.etNumber.text.clear()
            binding.etEmailSignup.text.clear()
            binding.etPasswordSignup.text.clear()
            binding.etPasswordConfirmation.text.clear()
        }
    }


    fun authentification() {
        auth.createUserWithEmailAndPassword(
            binding.etEmailSignup.text.toString(),
            binding.etPasswordSignup.text.toString()
        ).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()
                    ?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Email verifikasi telah dikirim. Silakan verifikasi email Anda.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val error = verificationTask.exception
                            Log.e("SignUpActivity", "Gagal mengirim email verifikasi: ${error?.message}")
                            Toast.makeText(
                                this@SignUpActivity,
                                "Gagal mengirim email verifikasi. Error: ${error?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    }
            } else {
                Toast.makeText(
                    this@SignUpActivity,
                    "Gagal registrasi. Pastikan email dan password valid.",
                    Toast.LENGTH_LONG
                ).show()
            }
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

        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Anda gagal daftar", Toast.LENGTH_SHORT)
                    .show()
                Log.e("daftar", " Gagal daftar karena :" + t.message)
            }

        })
    }


}