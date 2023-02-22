package com.example.shopit.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.models.OrderRequest
import com.example.shopit.models.PastOrderResponseItem
import com.example.shopit.models.RequestUserIdParameter
import com.example.shopit.repository.PastOrderRepository
import com.example.shopit.utils.NetworkResult
import kotlinx.coroutines.launch

class PastOrdersViewModel(private val pastOrderRepository: PastOrderRepository): ViewModel() {
    val pastOrderLivedata: LiveData<NetworkResult<List<PastOrderResponseItem>>>
        get() = pastOrderRepository.pastOrderLiveData

    val statusLiveData: LiveData<NetworkResult<String>>
        get() = pastOrderRepository.statusLiveData


    fun getPastOrders(userIdParameter: RequestUserIdParameter){
        viewModelScope.launch {
            pastOrderRepository.getPastOrderList(userIdParameter)
        }
    }

    fun addOrder(orderRequest: OrderRequest){
        viewModelScope.launch {
            pastOrderRepository.addOrder(orderRequest)
        }
    }
}