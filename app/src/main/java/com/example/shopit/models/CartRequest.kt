package com.example.shopit.models

data class CartRequest(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: Double,
    val title: String,
    val userId: String
)