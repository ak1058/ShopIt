package com.example.shopit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.shopit.R
import com.example.shopit.activities.MainActivity
import com.example.shopit.databinding.FragmentCartBinding
import com.example.shopit.databinding.FragmentDetailBinding
import com.example.shopit.models.CartRequest
import com.example.shopit.models.ProductItem
import com.example.shopit.utils.SavedDataPreference
import com.example.shopit.viewModels.CartViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var savedDataPreference: SavedDataPreference
    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        savedDataPreference = SavedDataPreference(requireActivity())
        cartViewModel = (activity as MainActivity).cartViewModel

        Log.d("product", savedDataPreference.getProductData().toString())
        // getting the json data from the sharedpreference of the beer item which is clicked
        val jsonProductData= savedDataPreference.getProductData()
        //parsing it and getting the productdata which is clicked
        val productData = Gson().fromJson(jsonProductData, ProductItem::class.java)
//
        Log.d("Product", productData.toString())
//        setting toolbar title the name of beer we get from the shared preference
        binding.toolBar.title = productData.category
        binding.apply {
            Glide.with(requireActivity())
                .load(productData.image)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(productImage)

            nameText.text = productData.title
            descText.text = productData.description
            ratingBar.rating = productData.rating.rate.toFloat()
            priceTextView.text = "$ "+productData.price.toString()
        }

        val userId = savedDataPreference.getUserId()
        val category = productData.category
        val description = productData.description
        val id = productData.id
        val image = productData.image
        val price = productData.price
        val title = productData.title

        val cartRequest = CartRequest(category,description,id,image,price,title,userId!!)

        Log.d("cartrequest", cartRequest.toString())

        binding.addToCartBtn.setOnClickListener {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    cartViewModel.addItemsToCart(cartRequest)
                }
            }catch (e: Exception){
                Log.e("CartAddingException", e.toString())
            }

            showMessage("Item added to Cart Successfully")

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val newFragment = CartFragment()
            fragmentTransaction?.replace(R.id.frame_layout, newFragment)
            fragmentManager?.popBackStack()
            fragmentTransaction?.commit()

        }

        binding.buyBtn.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val newFragment = PlaceOrderFragment()
            fragmentTransaction?.replace(R.id.frame_layout, newFragment)
            fragmentManager?.popBackStack()
            fragmentTransaction?.commit()
        }


        // setting back function on clicking back btn of toolbar
        binding.toolBar.setNavigationOnClickListener {

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }


        return binding.root
    }



    private fun showMessage(message: String){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

}