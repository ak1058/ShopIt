package com.example.shopit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shopit.databinding.ItemProductBinding
import com.example.shopit.listeners.Listener
import com.example.shopit.models.ProductItem

class ProductAdapter(val context: Context, val listener: Listener):
    ListAdapter<ProductItem, ProductAdapter.MyViewHolder>(diffUtil()) {

    class diffUtil: DiffUtil.ItemCallback<ProductItem>(){
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem==newItem
        }

    }

    class MyViewHolder(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.binding.apply {
            categoryText.text = currentItem.category
            nameTxt.text = currentItem.title
            ratingBar.rating = currentItem.rating.rate.toFloat()
            priceTextView.text = "$ " + currentItem.price.toString()
            Glide.with(context)
                .load(currentItem.image)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(productImage)

            root.setOnClickListener {
                    listener.onItemBtnClickListener(position, currentItem)
                }
            cartBtn.setOnClickListener {
                listener.onItemBtnClickListener(position, currentItem)
            }
        }
    }
}