package com.example.shopit.utils

import android.content.Context
import com.example.shopit.models.ProductItem
import com.example.shopit.utils.Constants.PRODUCT_KEY
import com.example.shopit.utils.Constants.SHARED_PREFERENCE_KEY
import com.example.shopit.utils.Constants.SHARED_PREFERENCE_KEY_2
import com.example.shopit.utils.Constants.SHARED_PREFERENCE_KEY_3
import com.example.shopit.utils.Constants.USERID_KEY
import com.example.shopit.utils.Constants.USERNAME_KEY
import com.google.gson.Gson

class SavedDataPreference(val context: Context) {

    val prefs = context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
    val prefs2 = context.getSharedPreferences(SHARED_PREFERENCE_KEY_2, Context.MODE_PRIVATE)
    val prefs3 = context.getSharedPreferences(SHARED_PREFERENCE_KEY_3, Context.MODE_PRIVATE)

    fun saveUserName(userName: String){
        val editor = prefs.edit()
        editor.putString(USERNAME_KEY, userName)
        editor.apply()
    }
    fun getUserName(): String? {
        return prefs.getString(USERNAME_KEY, " ")
    }

    fun saveUserId(userID: String){
        val editor = prefs2.edit()
        editor.putString(USERID_KEY, userID)
        editor.apply()
    }
    fun getUserId(): String? {
        return prefs2.getString(USERID_KEY, " ")
    }

    fun saveProductData(productItem: ProductItem){
        val editor = prefs3.edit()
        val parsedProductItem = Gson().toJson(productItem)
        editor.putString(PRODUCT_KEY, parsedProductItem)
        editor.apply()
    }
    fun getProductData(): String? {
        return prefs3.getString(PRODUCT_KEY, null)
    }
}