package com.cristianboicu.ebsproductapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cristianboicu.ebsproductapp.EndlessScrollListener
import com.cristianboicu.ebsproductapp.databinding.FragmentProductsBinding
import com.cristianboicu.ebsproductapp.ui.adapter.ProductsAdapter
import com.cristianboicu.ebsproductapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var binding: FragmentProductsBinding
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var lvProducts: ListView
    private lateinit var scrollListener: EndlessScrollListener
    private val TAG = "ProductsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        lvProducts = binding.lvProducts
        scrollListener = EndlessScrollListener(viewModel)
        setUpListView()

        viewModel.allProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        productsAdapter.submitList(it.results)

                        val totalPages = response.data.totalPages
                        scrollListener.setIsLastPage(viewModel.allProductsPage > totalPages)

                        Log.d(TAG, "${viewModel.allProductsPage}")
                    }
                }
                is Resource.Loading -> {
//                    Toast.makeText(context, "Is Loading", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Loading")
                }
                is Resource.Error -> {
                    Toast.makeText(context,
                        "An error occurred: ${response.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun setUpListView() {
        productsAdapter = ProductsAdapter(requireContext(), mutableListOf())
        lvProducts.apply {
            adapter = productsAdapter
            setOnScrollListener(scrollListener)
        }
    }

}