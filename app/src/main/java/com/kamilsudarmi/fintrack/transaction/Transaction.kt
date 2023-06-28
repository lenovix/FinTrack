package com.kamilsudarmi.fintrack.transaction

data class Transaction(
    val amount: Double,
    val category: String,
    val description: String,
    val date: String,
    val paymentMethod: String,
    val transactionType: String
) {
    constructor() : this(0.0, "", "", "", "","")
}