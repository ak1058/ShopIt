package com.example.shopit.api

import com.example.shopit.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EcommerceApi {

    @GET("/getAllProducts")
    suspend fun getAllProductList(): Response<List<ProductItem>>

    @POST("/cart/product/cartItems")
    suspend fun getItemsFromCart(@Body userIdParameter: RequestUserIdParameter): Response<List<CartModelListItem>>

    @POST("/orders/pastOrders/orders")
    suspend fun getPastOrders(@Body userIdParameter: RequestUserIdParameter): Response<List<PastOrderResponseItem>>

    @POST("/cart/product")
    suspend fun addItemsToCart(@Body cartRequest: CartRequest): Response<CartResponse>

    @POST("/cart/product/{id}")
    suspend fun deleteItemFromCart(@Path("id")noteId: Int, @Body userIdParameter: RequestUserIdParameter): Response<DeletedItem>

    @POST("/orders/pastOrders")
    suspend fun addToPastOrderList(@Body orderRequest: OrderRequest): Response<NewItem>
}