package com.kamilsudarmi.fintrack.auth.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.kamilsudarmi.fintrack.MainActivity
import com.kamilsudarmi.fintrack.auth.register.RegisterActivity
import com.kamilsudarmi.fintrack.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        FirebaseApp.initializeApp(this)

        button()
    }

    private fun button() {
        binding.btnLogin.setOnClickListener {
            val emailText = binding.etEmail.text.toString()
            val passwordText = binding.etPassword.text.toString()
            Log.d("login", "onCreate: $emailText $passwordText")
            signIn(emailText, passwordText)
        }
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Autentikasi sukses, lanjutkan ke tindakan berikutnya
                    // Contoh: Navigasi ke layar beranda
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                    // simpan 'remember me'
                    val sharedPreferences: SharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("remember_me", true)
                    editor.apply()
                } else {
                    // Autentikasi gagal, berikan pesan kesalahan kepada pengguna
                    val errorMessage = task.exception?.message
                    // Contoh: Tampilkan dialog error atau pesan toast
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    Log.d("error login: ", "$errorMessage")
                }
            }
    }
}