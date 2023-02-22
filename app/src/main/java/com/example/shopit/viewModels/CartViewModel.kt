package com.example.shopit.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.models.CartModelListItem
import com.example.shopit.models.CartRequest
import com.example.shopit.models.RequestUserIdParameter
import com.example.shopit.repository.CartRepository
import com.example.shopit.utils.NetworkResult
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository): ViewModel() {
    val cartLiveData: LiveData<NetworkResult<List<CartModelListItem>>>
        get() = cartRepository.cartLiveData

    val statusLiveData: LiveData<NetworkResult<String>>
        get() = cartRepository.statusLiveData

    fun getCartItems(userIdParameter: RequestUserIdParameter){
        viewModelScope.launch {
            cartRepository.getItemsFromCart(userIdParameter)
        }
    }

    fun addItemsToCart(cartRequest: CartRequest){
        viewModelScope.launch {
            cartRepository.addItemsToCart(cartRequest)
        }
    }

    fun deleteItemFromCart(itemId: Int, userIdParameter: RequestUserIdParameter){
        viewModelScope.launch {
            cartRepository.deleteItemFromCart(itemId, userIdParameter)
        }

    }
}