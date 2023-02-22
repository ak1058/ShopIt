package com.example.shopit.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shopit.R
import com.example.shopit.activities.WelcomeActivity
import com.example.shopit.databinding.FragmentCartBinding
import com.example.shopit.databinding.FragmentUserDetailBinding
import com.example.shopit.utils.SavedDataPreference
import com.google.firebase.auth.FirebaseAuth


class UserDetailFragment : Fragment() {

    private lateinit var binding: FragmentUserDetailBinding
    private lateinit var savedDataPreference: SavedDataPreference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        savedDataPreference = SavedDataPreference(requireActivity())
        auth = FirebaseAuth.getInstance()
        binding.apply {
            nameTxt.text = "Name: "+savedDataPreference.getUserName()
            emailText.text = "E-mail Address: "+auth.currentUser!!.email
        }
        binding.pastOrdersBtn.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val newFragment = PastOrdersFragment()
            fragmentTransaction?.replace(R.id.frame_layout, newFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }




        return binding.root
    }


}