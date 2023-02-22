package com.example.shopit.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.shopit.R
import com.example.shopit.activities.MainActivity
import com.example.shopit.databinding.FragmentPlaceOrderBinding
import com.example.shopit.models.OrderRequest
import com.example.shopit.models.ProductItem
import com.example.shopit.utils.NetworkResult
import com.example.shopit.utils.SavedDataPreference
import com.example.shopit.viewModels.PastOrdersViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaceOrderFragment : Fragment() {
    private lateinit var binding: FragmentPlaceOrderBinding
    private lateinit var pastOrdersViewModel: PastOrdersViewModel
    private lateinit var savedDataPreference: SavedDataPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaceOrderBinding.inflate(inflater, container, false)
        pastOrdersViewModel = (activity as MainActivity).pastOrdersViewModel
        savedDataPreference = SavedDataPreference(requireActivity())
        handleObserver()

        val jsonProductData= savedDataPreference.getProductData()
        //parsing it and getting the productdata which is clicked
        val productData = Gson().fromJson(jsonProductData, ProductItem::class.java)


        val userId = savedDataPreference.getUserId()


        val category = productData.category
        val description = productData.description
        val id = productData.id
        val image = productData.image
        val price = productData.price
        val title = productData.title



        binding.placeOrderBtn.setOnClickListener {
            if (binding.txtName.text.isNotEmpty()&&binding.txtAddress.text.isNotEmpty()&&binding.txtCity.text.isNotEmpty()&&binding.txtPhone.text.isNotEmpty()&&binding.txtPincode.text.isNotEmpty()&&binding.txtState.text.isNotEmpty()){
                val personName = binding.txtName.text.toString()
                val address = binding.txtAddress.text.toString()
                val orderRequest = OrderRequest(address,category,description,id,image,personName,price,title,userId!!)
                Log.d("orderRequest", orderRequest.toString())
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        pastOrdersViewModel.addOrder(orderRequest)
                    }catch (e: Exception){
                        Log.e("exception in placing order", e.toString())
                        Toast.makeText(requireActivity(), e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(requireActivity(), "Please fill All details.", Toast.LENGTH_SHORT).show()
            }
        }




        return binding.root
    }

    private fun handleObserver(){
        pastOrdersViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkResult.Success ->{
                    hideProgressBar()
                    showMessage("Order Successfully Placed.")
                    val fragmentManager = activity?.supportFragmentManager
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    val newFragment = HomeFragment()
                    fragmentTransaction?.replace(R.id.frame_layout, newFragment)
                    fragmentManager?.popBackStack()
                    fragmentTransaction?.commit()
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


}