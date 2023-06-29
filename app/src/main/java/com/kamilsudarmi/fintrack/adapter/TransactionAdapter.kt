package com.kamilsudarmi.fintrack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kamilsudarmi.fintrack.R
import com.kamilsudarmi.fintrack.transaction.Transaction
import java.text.DecimalFormat

class TransactionAdapter(private val transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAmount: TextView = itemView.findViewById(R.id.tv_amount)
//        val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)
    }

    private lateinit var auth: FirebaseAuth
    private fun deleteTransaction(transactionId: String, context: Context) {
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().reference
        val transactionRef = userId?.let { databaseReference.child("transactions").child(it).child(transactionId) }

        transactionRef?.removeValue()?.addOnSuccessListener {
            Toast.makeText(context, "Transaction deleted successfully", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
            Toast.makeText(context, "Failed to delete transaction", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransaction = transactionList[position]
        holder.tvAmount.text = formatCurrency(currentTransaction.amount)
//        holder.tvCategory.text = currentTransaction.category
        holder.tvDate.text = currentTransaction.date
        holder.tvDescription.text = currentTransaction.description

        // Mengecek tipe transaksi dan mengatur warna teks sesuai
        if (currentTransaction.transactionType == "Pengeluaran") {
            holder.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.context,
                R.color.red
            ))
        } else {
            holder.tvAmount.setTextColor(ContextCompat.getColor(holder.itemView.context,
                R.color.green
            ))
        }

        holder.btnDelete.setOnClickListener {
            val transactionId = currentTransaction.transactionId
            deleteTransaction(transactionId, holder.itemView.context)
        }

    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    private fun formatCurrency(amount: Double): String {
        val decimalFormat = DecimalFormat("#,###")
        return "Rp. ${decimalFormat.format(amount)}"
    }
}

