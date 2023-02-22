package com.example.shopit.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopit.repository.AllProductsRepository

class AllProductsViewModelFactory(private val allProductsRepository: AllProductsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllProductsViewModel(allProductsRepository) as T
    }
}