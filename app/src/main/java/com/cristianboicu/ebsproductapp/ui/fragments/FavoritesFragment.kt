package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.ListView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.databinding.FragmentFavoritesBinding
import com.cristianboicu.ebsproductapp.ui.activities.MainActivity
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
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        val menuHost: MenuHost = binding.layoutToolbar.toolbar

        setUpToolbar()
        setUpToolbarMenu(menuHost)

        lvFavoriteProducts = binding.lvFavoritesProducts
        setUpListView()

        displayFavoriteProducts()

        return binding.root
    }

    private fun displayFavoriteProducts() {
        viewModel.favoriteProducts.observe(viewLifecycleOwner) {
            productsAdapter.submitList(it)
        }
    }

    private fun setUpToolbarMenu(menuHost: MenuHost) {
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.findItem(R.id.favoritesFragment).icon =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.ic_full_heart)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner)
    }

    private fun setUpListView() {
        productsAdapter =
            ProductsAdapter(requireContext(),
                mutableListOf(),
                ::navigateToProductDetails,
                ::changeProductFavoriteStatus)
        lvFavoriteProducts.adapter = productsAdapter
    }

    private fun changeProductFavoriteStatus(product: ProductDomainModel) {
        viewModel.changeProductFavoriteStatus(product)
    }

    private fun navigateToProductDetails(productId: Long, productFavoriteStatus: Boolean) {
        findNavController().navigate(
            FavoritesFragmentDirections.showProductDetails(productId, productFavoriteStatus)
        )
    }

    private fun setUpToolbar() {
        val baseActivity = requireActivity() as MainActivity
        val toolbar = binding.layoutToolbar.toolbar
        baseActivity.setUpToolBar(toolbar)
    }

}