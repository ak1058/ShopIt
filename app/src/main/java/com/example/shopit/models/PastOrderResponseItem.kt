package com.example.shopit.models

data class PastOrderResponseItem(
    val __v: Int,
    val _id: String,
    val address: String,
    val category: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val image: String,
    val name: String,
    val price: Double,
    val title: String,
    val updatedAt: String,
    val userId: String
)