package com.kamilsudarmi.fintrack.transaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kamilsudarmi.fintrack.MainActivity
import com.kamilsudarmi.fintrack.R
import com.kamilsudarmi.fintrack.databinding.ActivityAddTransactionBinding
import com.kamilsudarmi.fintrack.databinding.ActivityMainBinding

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        button()
    }

    private fun button() {
        binding.btnAddTransaction.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}