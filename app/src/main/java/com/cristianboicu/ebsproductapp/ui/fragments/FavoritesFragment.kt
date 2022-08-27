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
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.databinding.FragmentFavoritesBinding
import com.cristianboicu.ebsproductapp.ui.MainActivity
import com.cristianboicu.ebsproductapp.ui.adapter.ProductsAdapter
import com.cristianboicu.ebsproductapp.ui.viewModels.FavoriteProductsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoriteProductsViewModel by viewModels()
    private lateinit var lvFavoriteProducts: ListView
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        setUpToolbar()

        lvFavoriteProducts = binding.lvFavoritesProducts
        setUpListView()

        viewModel.favoriteProducts.observe(viewLifecycleOwner) {
            productsAdapter.submitList(it)
            Log.d("FavoritesFragment", it.toString())
        }

        return binding.root
    }

    private fun setUpListView() {
        productsAdapter =
            ProductsAdapter(requireContext(),
                mutableListOf(),
                ::navigateToProductDetails,
                ::saveProductToFavorites)
        lvFavoriteProducts.adapter = productsAdapter
    }

    private fun saveProductToFavorites(product: ProductDomainModel) {
        viewModel.changeProductFavoriteStatus(product)
        Toast.makeText(context,
            "Add ${product.name} to favorites",
            Toast.LENGTH_SHORT).show()
    }

    private fun navigateToProductDetails(productId: Long) {
        findNavController().navigate(
            FavoritesFragmentDirections.showProductDetails(productId)
        )
    }

    private fun setUpToolbar() {
        val baseActivity = requireActivity() as MainActivity
        val toolbar = binding.layoutToolbar.toolbar
        baseActivity.setUpToolBar(toolbar)
    }

}