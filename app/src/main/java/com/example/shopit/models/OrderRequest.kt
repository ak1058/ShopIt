package com.example.shopit.models

data class OrderRequest(
    val address: String,
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Double,
    val title: String,
    val userId: String
)