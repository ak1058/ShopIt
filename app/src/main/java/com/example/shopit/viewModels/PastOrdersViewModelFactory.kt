package com.example.shopit.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopit.repository.PastOrderRepository

class PastOrdersViewModelFactory(private val pastOrderRepository: PastOrderRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PastOrdersViewModel(pastOrderRepository) as T
    }
}