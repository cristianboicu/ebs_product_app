package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cristianboicu.ebsproductapp.EndlessScrollListener
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        scrollListener = EndlessScrollListener(viewModel)
        val menuHost: MenuHost = binding.layoutToolbar.toolbar

        lvProducts = binding.lvProducts
        setUpListView()
        setUpToolbarMenu(menuHost)


        displayAllProducts()
        viewModel.favoriteProducts.observe(viewLifecycleOwner) {
        }
        return binding.root
    }

    private fun displayAllProducts() {
        viewModel.allProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        productsAdapter.submitList(it.results)
                        val totalPages = response.data.totalPages
                        scrollListener.setIsLastPage(viewModel.allProductsPage > totalPages)
                    }
                }
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                    Toast.makeText(context,
                        "An error occurred: ${response.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUpToolbarMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.favoritesFragment -> {
                        navigateToFavorites()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    override fun onStart() {
        super.onStart()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        val baseActivity = requireActivity() as MainActivity
        val toolbar = binding.layoutToolbar.toolbar
        baseActivity.setUpToolBar(toolbar)
    }

    private fun setUpListView() {
        productsAdapter =
            ProductsAdapter(requireContext(),
                mutableListOf(),
                ::navigateToProductDetails,
                ::changeProductFavoriteStatus)
        lvProducts.apply {
            adapter = productsAdapter
            setOnScrollListener(scrollListener)
        }
    }

    private fun changeProductFavoriteStatus(product: ProductDomainModel) {
        viewModel.changeProductFavoriteStatus(product)
    }

    private fun navigateToProductDetails(productId: Long) {
        findNavController().navigate(
            ProductsFragmentDirections.showProductDetails(productId)
        )
    }

    private fun navigateToFavorites() {
        findNavController().navigate(
            ProductsFragmentDirections.showFavorites()
        )
    }
}