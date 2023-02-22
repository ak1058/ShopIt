package com.example.shopit.listeners

import com.example.shopit.models.CartModelListItem
import com.example.shopit.models.ProductItem

interface Listener {

    fun onItemBtnClickListener(position: Int, productItem: ProductItem)

    fun onDeleteBtnClickListener(position: Int, cartModelListItem: CartModelListItem)
}