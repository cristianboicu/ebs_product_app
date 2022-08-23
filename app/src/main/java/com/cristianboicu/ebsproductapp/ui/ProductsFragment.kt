package com.cristianboicu.ebsproductapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.cristianboicu.ebsproductapp.databinding.FragmentProductsBinding
import com.cristianboicu.ebsproductapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var binding: FragmentProductsBinding
    private val TAG = "ProductsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProductsBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.allProducts.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Log.d(TAG, response.data?.results.toString())
                }
                is Resource.Loading -> {
                    Log.d(TAG, "Loading")

                }
                is Resource.Error -> {
                    Log.d(TAG, "Error: ${response.message.toString()}")
                }
            }
        })

        return binding.root
    }

}