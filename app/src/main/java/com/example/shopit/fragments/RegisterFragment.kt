package com.example.shopit.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.shopit.R
import com.example.shopit.activities.MainActivity
import com.example.shopit.databinding.FragmentRegisterBinding
import com.example.shopit.utils.SavedDataPreference
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var savedDataPreference: SavedDataPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        savedDataPreference = SavedDataPreference(requireActivity().applicationContext)

        binding.alreadyHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.registerBtn.setOnClickListener {
            showProgressBar()
            registerAccount()
        }
        return binding.root
    }

    private fun showMessage(message: String){
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun registerAccount(){
        val email = binding.emailText.editText?.text.toString()
        val password = binding.passwordText.editText?.text.toString()
        val username = binding.userNameText.editText?.text.toString()

        savedDataPreference.saveUserName(username)

        if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    hideProgressBar()
                    savedDataPreference.saveUserId(auth.currentUser!!.uid)
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }.addOnFailureListener {
                hideProgressBar()
                showMessage(it.localizedMessage!!)
            }
        }else{
            showMessage("Please provide complete details.")
            hideProgressBar()
        }

    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.GONE
    }

}