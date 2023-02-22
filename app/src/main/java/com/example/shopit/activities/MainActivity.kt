package com.example.shopit.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shopit.R
import com.example.shopit.databinding.ActivityMainBinding
import com.example.shopit.fragments.CartFragment
import com.example.shopit.fragments.HomeFragment
import com.example.shopit.fragments.UserDetailFragment
import com.example.shopit.repository.AllProductsRepository
import com.example.shopit.repository.CartRepository
import com.example.shopit.repository.PastOrderRepository
import com.example.shopit.viewModels.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var allProductsViewModel: AllProductsViewModel
    lateinit var cartViewModel: CartViewModel
    lateinit var pastOrdersViewModel: PastOrdersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        val productRepository = AllProductsRepository()
        val factory = AllProductsViewModelFactory(productRepository)
        allProductsViewModel = ViewModelProvider(this, factory)[AllProductsViewModel::class.java]

        val cartRepository = CartRepository()
        val cartFactory = CartViewModelFactory(cartRepository)
        cartViewModel = ViewModelProvider(this, cartFactory)[CartViewModel::class.java]

        val pastOrderRepository = PastOrderRepository()
        val orderFactory = PastOrdersViewModelFactory(pastOrderRepository)
        pastOrdersViewModel = ViewModelProvider(this, orderFactory)[PastOrdersViewModel::class.java]

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeMenu -> replaceFragment(HomeFragment())
                R.id.cartMenu ->{replaceFragment(CartFragment())}
                R.id.profileMenu ->{replaceFragment(UserDetailFragment())}

            }
            true
        }






    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}