package com.example.shopit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopit.R
import com.example.shopit.activities.MainActivity
import com.example.shopit.adapters.CartAdapter
import com.example.shopit.databinding.FragmentCartBinding
import com.example.shopit.listeners.Listener
import com.example.shopit.models.CartModelListItem
import com.example.shopit.models.ProductItem
import com.example.shopit.models.RequestUserIdParameter
import com.example.shopit.utils.NetworkResult
import com.example.shopit.utils.SavedDataPreference
import com.example.shopit.viewModels.CartViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartFragment : Fragment(), Listener {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var savedDataPreference: SavedDataPreference
    private lateinit var productData: ProductItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)
        cartAdapter = CartAdapter(requireActivity(), this)
        cartViewModel = (activity as MainActivity).cartViewModel
        savedDataPreference = SavedDataPreference(requireActivity())
        handleObserver()
        handleCartObserver()
        val jsonProductData= savedDataPreference.getProductData()
        //parsing it and getting the productdata which is clicked
        productData = Gson().fromJson(jsonProductData, ProductItem::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            cartViewModel.getCartItems(RequestUserIdParameter(savedDataPreference.getUserId()!!))
        }


        binding.cartRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager =linearLayoutManager
            setHasFixedSize(true)
            adapter = cartAdapter
        }

        binding.toolBar.setNavigationOnClickListener {

            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val newFragment = HomeFragment()
            fragmentTransaction?.replace(R.id.frame_layout, newFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        return binding.root
    }


    private fun handleObserver(){
        cartViewModel.cartLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkResult.Success ->{
                    hideProgressBar()
                    cartAdapter.submitList(it.data)
                }

                is NetworkResult.Error -> {
                    hideProgressBar()
                    showMessage(it.message!!)
                }

                is NetworkResult.Loading ->{
                    showProgressBar()
                }
            }
        })
    }


    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
    }

    private fun showMessage(message: String){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemBtnClickListener(position: Int, productItem: ProductItem) {
        return
    }

    override fun onDeleteBtnClickListener(position: Int, cartModelListItem: CartModelListItem) {
//        Log.d("id", productData.id.toString())
//        Log.d("userId", savedDataPreference.getUserId()!!.toString())
//        CoroutineScope(Dispatchers.IO).launch {
//            cartViewModel.deleteItemFromCart(productData.id, RequestUserIdParameter(savedDataPreference.getUserId()!!))
//        }
        return
    }

    private fun handleCartObserver(){
        cartViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkResult.Success ->{
                    hideProgressBar()
                    showMessage("Item deleted Successfully.")
                }

                is NetworkResult.Error -> {
                    hideProgressBar()
                    showMessage(it.message!!)
                }

                is NetworkResult.Loading ->{
                    showProgressBar()
                }
            }
        })
    }


}