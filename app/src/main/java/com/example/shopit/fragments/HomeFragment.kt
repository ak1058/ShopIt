package com.example.shopit.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopit.R
import com.example.shopit.activities.MainActivity
import com.example.shopit.adapters.ProductAdapter
import com.example.shopit.databinding.FragmentHomeBinding
import com.example.shopit.listeners.Listener
import com.example.shopit.models.CartModelListItem
import com.example.shopit.models.ProductItem
import com.example.shopit.utils.NetworkResult
import com.example.shopit.utils.SavedDataPreference
import com.example.shopit.viewModels.AllProductsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), Listener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var allProductsViewModel: AllProductsViewModel
    private lateinit var productAdapter: ProductAdapter
    private lateinit var savedDataPreference: SavedDataPreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        allProductsViewModel = (activity as MainActivity).allProductsViewModel
        productAdapter = ProductAdapter(requireActivity(), this)
        savedDataPreference = SavedDataPreference(requireActivity())
        handleObserver()

        CoroutineScope(Dispatchers.IO).launch {
            allProductsViewModel.getAllProducts()
        }


        binding.productRecyclerView.apply {
            setHasFixedSize(true)
            adapter = productAdapter
        }
        binding.welcomeText.text = "Welcome " + savedDataPreference.getUserName()





        return binding.root
    }

    private fun handleObserver(){
        allProductsViewModel.productLivedata.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkResult.Success ->{
                    hideProgressBar()
                    productAdapter.submitList(it.data)
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
        savedDataPreference.saveProductData(productItem)



        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        val newFragment = DetailFragment()
        fragmentTransaction?.replace(R.id.frame_layout, newFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    override fun onDeleteBtnClickListener(position: Int, cartModelListItem: CartModelListItem) {
        return
    }


}