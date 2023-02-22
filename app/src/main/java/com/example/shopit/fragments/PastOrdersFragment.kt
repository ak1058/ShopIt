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
import com.example.shopit.adapters.PastOrdersAdapter
import com.example.shopit.databinding.FragmentCartBinding
import com.example.shopit.databinding.FragmentPastOrdersBinding
import com.example.shopit.models.RequestUserIdParameter
import com.example.shopit.utils.NetworkResult
import com.example.shopit.utils.SavedDataPreference
import com.example.shopit.viewModels.PastOrdersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PastOrdersFragment : Fragment() {

    private lateinit var binding: FragmentPastOrdersBinding
    private lateinit var pastOrdersViewModel: PastOrdersViewModel
    private lateinit var pastOrdersAdapter: PastOrdersAdapter
    private lateinit var savedDataPreference: SavedDataPreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPastOrdersBinding.inflate(inflater, container, false)
        pastOrdersViewModel = (activity as MainActivity).pastOrdersViewModel
        pastOrdersAdapter = PastOrdersAdapter(requireActivity())
        savedDataPreference = SavedDataPreference(requireActivity())
        handleObserver()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                pastOrdersViewModel.getPastOrders(RequestUserIdParameter(savedDataPreference.getUserId()!!))
            }catch (e: Exception){
                Log.e("exceptionInPastorders", e.toString())
                showMessage(e.toString())
            }

        }

        binding.pastOrderRecyclerView.apply {
            val linearLayoutManager = LinearLayoutManager(activity?.applicationContext)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            layoutManager =linearLayoutManager
            setHasFixedSize(true)
            adapter = pastOrdersAdapter
        }

        binding.toolBar.setNavigationOnClickListener {

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }



        return binding.root
    }

    private fun handleObserver(){
        pastOrdersViewModel.pastOrderLivedata.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkResult.Success ->{
                    hideProgressBar()
                    pastOrdersAdapter.submitList(it.data)
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