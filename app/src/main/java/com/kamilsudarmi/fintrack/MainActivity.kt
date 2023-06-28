package com.kamilsudarmi.fintrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.kamilsudarmi.fintrack.auth.login.LoginActivity
import com.kamilsudarmi.fintrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        FirebaseApp.initializeApp(this)

        checkLoginStatus()
        button()
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        val rememberMe = sharedPreferences.getBoolean("remember_me", false)
        if (!rememberMe) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun button() {
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // fungsi logout
    }
}