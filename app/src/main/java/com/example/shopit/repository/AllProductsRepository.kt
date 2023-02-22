package com.example.shopit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopit.api.RetrofitHelper
import com.example.shopit.models.ProductItem
import com.example.shopit.utils.NetworkResult
import org.json.JSONObject

class AllProductsRepository {

    private val allProductsMutableLiveData = MutableLiveData<NetworkResult<List<ProductItem>>>()
    val allProductsLiveData: LiveData<NetworkResult<List<ProductItem>>>
        get() = allProductsMutableLiveData

    suspend fun getAllProducts(){
        allProductsMutableLiveData.postValue(NetworkResult.Loading())
        try {
            val response = RetrofitHelper.ecommerceApi.getAllProductList()

            if (response.isSuccessful && response.body()!=null){
                allProductsMutableLiveData.postValue(NetworkResult.Success(response.body()!!))
            }else if (response.errorBody()!=null){
                val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
                allProductsMutableLiveData.postValue(NetworkResult.Error(errorObject.getString("message")))
            }else{
                allProductsMutableLiveData.postValue(NetworkResult.Error("Something went wrong while getting Products"))
            }
        }catch (e: Exception){
            Log.e("exceptionInProductRepo", e.toString())
        }

    }
}