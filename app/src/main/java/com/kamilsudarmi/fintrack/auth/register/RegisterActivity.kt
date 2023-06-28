package com.kamilsudarmi.fintrack.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.kamilsudarmi.fintrack.auth.login.LoginActivity
import com.kamilsudarmi.fintrack.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        FirebaseApp.initializeApp(this)

        button()
    }

    private fun button() {
        binding.btnRegister.setOnClickListener {
            val emailText = binding.etEmail.text.toString()
            val passwordText = binding.etPassword.text.toString()
            val passwordConfirm = binding.etConfirmPassword.text.toString()
            if (passwordText == passwordConfirm){
                signUp(emailText, passwordConfirm)
            }else{
                Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signUp(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Pembuatan akun sukses, lanjutkan ke tindakan berikutnya
                    // Contoh: Navigasi ke layar pengisian profil
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Pembuatan akun gagal, berikan pesan kesalahan kepada pengguna
                    val errorMessage = task.exception?.message
                    // Contoh: Tampilkan dialog error atau pesan toast
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}