package com.example.shopit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopit.api.RetrofitHelper
import com.example.shopit.models.CartModelListItem
import com.example.shopit.models.OrderRequest
import com.example.shopit.models.PastOrderResponseItem
import com.example.shopit.models.RequestUserIdParameter
import com.example.shopit.utils.NetworkResult
import org.json.JSONObject

class PastOrderRepository {

    private val pastOrderMutableLiveData = MutableLiveData<NetworkResult<List<PastOrderResponseItem>>>()
    val pastOrderLiveData: LiveData<NetworkResult<List<PastOrderResponseItem>>>
        get() = pastOrderMutableLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getPastOrderList(userIdParameter: RequestUserIdParameter){
        pastOrderMutableLiveData.postValue(NetworkResult.Loading())
        try {
            val response = RetrofitHelper.ecommerceApi.getPastOrders(userIdParameter)
            if (response.isSuccessful && response.body()!=null){
                pastOrderMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
            }else if (response.errorBody()!=null){
                val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
                pastOrderMutableLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))
            }else{
                pastOrderMutableLiveData.postValue(NetworkResult.Error("Something went wrong while getting Past Orders."))
            }
        }catch (e: Exception){
            Log.e("exceptionInOrderRepo", e.toString())
        }

    }

    suspend fun addOrder(orderRequest: OrderRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = RetrofitHelper.ecommerceApi.addToPastOrderList(orderRequest)
            if (response.isSuccessful && response.body()!=null){
                _statusLiveData.postValue(NetworkResult.Success("Order Placed"))
            }else{
                _statusLiveData.postValue(NetworkResult.Error("Some error occurred while placing order."))
            }
        }catch (e: Exception){
            Log.e("exceptionInOrderRepo", e.toString())
        }

    }
}