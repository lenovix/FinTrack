package com.kamilsudarmi.fintrack.transaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
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
        spinnerCategory()
        spinnerPaymentMethod()
    }

    private fun spinnerPaymentMethod() {
        val spinnerPaymentMethod: Spinner = binding.spPaymentMethod

        // Ambil data metode pembayaran dari string-array payment_methods
        val paymentMethods = resources.getStringArray(R.array.payment_methods)

        // Buat adapter untuk Spinner menggunakan array tersebut
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)

        // Atur tampilan dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Terapkan adapter pada Spinner
        spinnerPaymentMethod.adapter = adapter
    }

    private fun spinnerCategory() {
        val spinnerCategory: Spinner = binding.spCategory

        // Ambil data kategori dari string-array transaction_categories
        val categories = resources.getStringArray(R.array.transaction_categories)

        // Buat adapter untuk Spinner menggunakan array tersebut
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)

        // Atur tampilan dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Terapkan adapter pada Spinner
        spinnerCategory.adapter = adapter
    }

    private fun button() {
        binding.btnAddTransaction.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}