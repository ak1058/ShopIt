package com.example.shopit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shopit.databinding.ItemCartBinding
import com.example.shopit.databinding.ItemProductBinding
import com.example.shopit.listeners.Listener
import com.example.shopit.models.CartModelListItem
import com.example.shopit.models.ProductItem

class CartAdapter(val context: Context, val listener: Listener): ListAdapter<CartModelListItem, CartAdapter.MyViewHolder>(diffUtil()) {

    class diffUtil: DiffUtil.ItemCallback<CartModelListItem>(){
        override fun areItemsTheSame(oldItem: CartModelListItem, newItem: CartModelListItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: CartModelListItem, newItem: CartModelListItem): Boolean {
            return oldItem==newItem
        }

    }

    class MyViewHolder(val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.apply {
            Glide.with(context)
                .load(currentItem.image)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(productImage)

            nameText.text = currentItem.title
            priceTextView.text = "$ "+currentItem.price.toString()
            categoryText.text = currentItem.category
            deleteBtn.visibility = View.GONE
            deleteBtn.setOnClickListener {
                listener.onDeleteBtnClickListener(position, currentItem)
            }
        }
    }
}