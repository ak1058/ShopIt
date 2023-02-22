package com.example.shopit.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.models.ProductItem
import com.example.shopit.repository.AllProductsRepository
import com.example.shopit.utils.NetworkResult
import kotlinx.coroutines.launch

class AllProductsViewModel(private val allProductsRepository: AllProductsRepository): ViewModel() {
    val productLivedata: LiveData<NetworkResult<List<ProductItem>>>
        get() = allProductsRepository.allProductsLiveData

    fun getAllProducts(){
        viewModelScope.launch {
            allProductsRepository.getAllProducts()
        }
    }

}