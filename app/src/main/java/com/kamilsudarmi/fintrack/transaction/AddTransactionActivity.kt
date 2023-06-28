package com.kamilsudarmi.fintrack.transaction

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kamilsudarmi.fintrack.R
import com.kamilsudarmi.fintrack.databinding.ActivityAddTransactionBinding
import java.util.Calendar

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    private val calendar: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        val currencyTextWatcher = CurrencyTextWatcher(binding.etAmount)
//        binding.etAmount.addTextChangedListener(currencyTextWatcher)

        // Inisialisasi Firebase Auth dan Database
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

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
            addTransaction()
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
        }
        binding.btnPickDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                // Mengatur tanggal yang dipilih ke TextView tvDate
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                binding.tvDate.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Menampilkan DatePickerDialog
        datePickerDialog.show()
    }

    private fun addTransaction() {
        // Mendapatkan input dari pengguna
        val amount = binding.etAmount.text.toString().toDouble()
        val category = binding.spCategory.selectedItem.toString()
        val description = binding.etDescription.text.toString()
        val date = binding.tvDate.text.toString()
        val paymentMethod = binding.spPaymentMethod.selectedItem.toString()
        // Mendapatkan jenis transaksi (pengeluaran atau pemasukan)
        val transactionType = if (binding.radioExpense.isChecked) "Pengeluaran" else "Pemasukan"

        // Mendapatkan ID pengguna saat ini
        val userId = firebaseAuth.currentUser?.uid

        // Mengecek apakah ID pengguna tidak null
        if (userId != null) {
            // Membuat referensi database untuk data transaksi pengguna
            val userTransactionRef = firebaseDatabase.reference.child("transactions").child(userId)

            // Membuat objek Transaction
            val transaction = Transaction(amount, category, description, date, paymentMethod, transactionType)

            // Menyimpan data transaksi ke Firebase Database
            val transactionRef = userTransactionRef.push()
            transactionRef.setValue(transaction)
                .addOnSuccessListener {
                    Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show()
                    finish() // Menutup Activity setelah transaksi berhasil ditambahkan
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add transaction: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}