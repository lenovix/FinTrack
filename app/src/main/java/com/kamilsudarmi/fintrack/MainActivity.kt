package com.kamilsudarmi.fintrack

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kamilsudarmi.fintrack.adapter.TransactionAdapter
import com.kamilsudarmi.fintrack.auth.login.LoginActivity
import com.kamilsudarmi.fintrack.databinding.ActivityMainBinding
import com.kamilsudarmi.fintrack.transaction.AddTransactionActivity
import com.kamilsudarmi.fintrack.transaction.Transaction
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        FirebaseApp.initializeApp(this)

        // Inisialisasi Firebase Auth dan Database
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        checkLoginStatus()
        button()
    }

    fun formatCurrency(amount: Double): String {
        val decimalFormat = DecimalFormat("#,###")
        return "Rp. ${decimalFormat.format(amount)}"
    }

    override fun onStart() {
        super.onStart()
        // Mengecek apakah pengguna sudah login
        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        if (currentUser != null) {
            // Mendapatkan ID pengguna saat ini
            val userId = currentUser.uid

            // Mendapatkan referensi database untuk data transaksi pengguna
            val userTransactionRef: DatabaseReference =
                firebaseDatabase.reference.child("transactions").child(userId)

            // Mendapatkan total pemasukan
            userTransactionRef.orderByChild("transactionType").equalTo("Pemasukan")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var totalIncome = 0.0
                        for (transactionSnapshot in dataSnapshot.children) {
                            val transaction = transactionSnapshot.getValue(Transaction::class.java)
                            transaction?.let {
                                totalIncome += it.amount
                            }
                        }
                        binding.tvTotalIncome.text = formatCurrency(totalIncome)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })

            // Mendapatkan total pengeluaran
            userTransactionRef.orderByChild("transactionType").equalTo("Pengeluaran")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var totalExpense = 0.0
                        for (transactionSnapshot in dataSnapshot.children) {
                            val transaction = transactionSnapshot.getValue(Transaction::class.java)
                            transaction?.let {
                                totalExpense += it.amount
                            }
                        }
                        binding.tvTotalExpense.text = formatCurrency(totalExpense)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })

            // Mendapatkan total uang pengguna
            userTransactionRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var totalMoney = 0.0
                    for (transactionSnapshot in dataSnapshot.children) {
                        val transaction = transactionSnapshot.getValue(Transaction::class.java)
                        transaction?.let {
                            if (it.transactionType == "Pemasukan") {
                                totalMoney += it.amount
                            } else {
                                totalMoney -= it.amount
                            }
                        }
                    }
                    binding.tvTotalMoney.text = formatCurrency(totalMoney)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })

            val recyclerView: RecyclerView = findViewById(R.id.recycler_view_transactions)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this)

            val transactionList = ArrayList<Transaction>()
            val transactionAdapter = TransactionAdapter(transactionList)
            recyclerView.adapter = transactionAdapter

            userTransactionRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    transactionList.clear()

                    for (transactionSnapshot in snapshot.children) {
                        val transaction = transactionSnapshot.getValue(Transaction::class.java)
                        transaction?.let {
                            transactionList.add(it)
                        }
                    }
                    transactionAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Menangani kegagalan saat membaca data dari Firebase
                }
            })

        }
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
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logout() {
        // fungsi logout
    }
}