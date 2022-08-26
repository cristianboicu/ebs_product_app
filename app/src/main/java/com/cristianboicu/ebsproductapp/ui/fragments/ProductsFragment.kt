package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cristianboicu.ebsproductapp.EndlessScrollListener
import com.cristianboicu.ebsproductapp.data.model.Product
import com.cristianboicu.ebsproductapp.databinding.FragmentProductsBinding
import com.cristianboicu.ebsproductapp.ui.MainActivity
import com.cristianboicu.ebsproductapp.ui.adapter.ProductsAdapter
import com.cristianboicu.ebsproductapp.ui.viewModels.ProductsViewModel
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
                    hideProgressBar()
                    response.data?.let {
                        productsAdapter.submitList(it.results)

                        val totalPages = response.data.totalPages
                        scrollListener.setIsLastPage(viewModel.allProductsPage > totalPages)

                        Log.d(TAG, "${viewModel.allProductsPage}")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
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

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        val baseActivity = requireActivity() as MainActivity
        val toolbar = binding.layoutToolbar.toolbar
        baseActivity.setUpToolBar(toolbar)
    }

    private fun setUpListView() {
        productsAdapter =
            ProductsAdapter(requireContext(),
                mutableListOf(),
                ::navigateToProductDetails,
                ::saveProductToFavorites)
        lvProducts.apply {
            adapter = productsAdapter
            setOnScrollListener(scrollListener)
        }
    }

    private fun saveProductToFavorites(product: Product) {
        Toast.makeText(context,
            "Add ${product.name} to favorites",
            Toast.LENGTH_SHORT).show()
    }

    private fun navigateToProductDetails(productId: Long) {
        findNavController().navigate(
            ProductsFragmentDirections.showProductDetails(productId)
        )
    }


}