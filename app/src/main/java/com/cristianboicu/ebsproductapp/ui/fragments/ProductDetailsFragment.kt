package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.ProductDetails
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.data.model.ProductImage
import com.cristianboicu.ebsproductapp.databinding.FragmentProductDetailsBinding
import com.cristianboicu.ebsproductapp.ui.MainActivity
import com.cristianboicu.ebsproductapp.ui.adapter.ImageSliderAdapter
import com.cristianboicu.ebsproductapp.ui.viewModels.ProductDetailsViewModel
import com.cristianboicu.ebsproductapp.util.Resource
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailsViewModel by viewModels()
    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater)

        val menuHost: MenuHost = binding.layoutToolbar.toolbar
        val productId = ProductDetailsFragmentArgs.fromBundle(requireArguments()).productId
        val mainActivity = requireActivity() as MainActivity

        setUpToolbar(mainActivity)
        setUpToolbarMenu(menuHost)
        hideToolbarLogo(toolbar)

        viewModel.getProductDetails(productId)
        showProductDetails()

        return binding.root
    }

    private fun showProductDetails() {
        viewModel.productDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { product ->
                        fillProductDetails(product)
                        setUpPager(product.images as MutableList<ProductImage>)
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
                        toggleFavoriteIcon(menuItem)
                        saveProductToFavorites(menuItem.isChecked)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun saveProductToFavorites(favorites: Boolean) {
        viewModel.changeProductFavoriteStatus(favorites)
    }

    private fun toggleFavoriteIcon(menuItem: MenuItem) {
        menuItem.isChecked = !menuItem.isChecked
        when (menuItem.isChecked) {
            true -> menuItem.icon =
                ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_full_heart)
            false -> menuItem.icon =
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_heart)
        }
    }

    private fun setUpToolbar(mainActivity: MainActivity) {
        toolbar = binding.layoutToolbar.toolbar
        mainActivity.setUpToolBar(toolbar)
    }

    private fun hideToolbarLogo(toolbar: Toolbar) {
        toolbar.findViewById<ImageView>(R.id.iv_logo).visibility = View.GONE
        toolbar.findViewById<TabLayout>(R.id.tab_layout).visibility = View.VISIBLE
    }

    private fun setUpPager(images: MutableList<ProductImage>) {
        images.add(images[0])
        images.add(images[0])
        images.add(images[0])

        val pager = binding.viewpager
        val adapter: PagerAdapter = ImageSliderAdapter(requireContext(), images)
        pager.adapter = adapter

        setUpPagerIndicator(pager)
    }

    private fun setUpPagerIndicator(pager: ViewPager) {
        val tabLayout = toolbar.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.visibility = View.VISIBLE
        tabLayout.setupWithViewPager(pager, true)
    }

    private fun fillProductDetails(product: ProductDetails) {
        binding.tvProductDetailsInfo.text = product.details
        binding.tvProductDetailsName.text = product.name
        binding.tvProductPrice.text =
            String.format(requireContext().getString(R.string.product_price),
                product.price.toString())
        binding.tvProductPriceSecond.text =
            String.format(requireContext().getString(R.string.product_price),
                product.price.toString())
        binding.tvProductDetailsSpecs.text = product.size
        binding.layoutToolbar.toolbar.menu.findItem(R.id.favoritesFragment).isChecked = product.favorite
    }

}