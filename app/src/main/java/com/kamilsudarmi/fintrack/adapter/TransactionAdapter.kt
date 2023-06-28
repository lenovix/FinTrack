package com.kamilsudarmi.fintrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    private fun formatCurrency(amount: Double): String {
        val decimalFormat = DecimalFormat("#,###")
        return "Rp. ${decimalFormat.format(amount)}"
    }
}

