package com.example.shopit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopit.api.RetrofitHelper
import com.example.shopit.models.CartModelListItem
import com.example.shopit.models.CartRequest
import com.example.shopit.models.RequestUserIdParameter
import com.example.shopit.utils.NetworkResult
import org.json.JSONObject


class CartRepository {
    private val cartMutableLiveData = MutableLiveData<NetworkResult<List<CartModelListItem>>>()
    val cartLiveData: LiveData<NetworkResult<List<CartModelListItem>>>
        get() = cartMutableLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getItemsFromCart(userIdParameter: RequestUserIdParameter){
        cartMutableLiveData.postValue(NetworkResult.Loading())
        try {
            val response = RetrofitHelper.ecommerceApi.getItemsFromCart(userIdParameter)
            if (response.isSuccessful && response.body()!=null){
                cartMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
            }else if (response.errorBody()!=null){
                val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
                cartMutableLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))
            }else{
                cartMutableLiveData.postValue(NetworkResult.Error("Something went wrong while getting Cart Items"))
            }
        }catch (e: Exception){
            Log.e("exceptionInCartRepo", e.toString())
        }

    }

    suspend fun addItemsToCart(cartRequest: CartRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = RetrofitHelper.ecommerceApi.addItemsToCart(cartRequest)
            if (response.isSuccessful && response.body()!=null){
                _statusLiveData.postValue(NetworkResult.Success("Item added to Cart successfully."))
            }else{
                _statusLiveData.postValue(NetworkResult.Error("Some error occurred while adding items to the cart."))
            }
        }catch (e: Exception){
            Log.e("exceptionInCartRepo", e.toString())
        }

    }

    suspend fun deleteItemFromCart(itemId: Int, userIdParameter: RequestUserIdParameter){
        _statusLiveData.postValue(NetworkResult.Loading())
        try {
            val response = RetrofitHelper.ecommerceApi.deleteItemFromCart(itemId, userIdParameter)
            if (response.isSuccessful && response.body()!=null){
                _statusLiveData.postValue(NetworkResult.Success("Item deleted successfully."))
            }else{
                _statusLiveData.postValue(NetworkResult.Error("Some error occured in deleting the item"))

            }
        }catch (e: Exception){
            Log.e("exceptionInCartRepo", e.toString())
        }
    }



}