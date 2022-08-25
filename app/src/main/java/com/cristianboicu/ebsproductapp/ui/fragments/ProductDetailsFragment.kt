package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.Product
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
    private lateinit var baseActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        val productId = ProductDetailsFragmentArgs.fromBundle(requireArguments()).productId

        baseActivity = requireActivity() as MainActivity
        baseActivity.hideToolbarLogo()

        viewModel.getProductDetails(productId)
        viewModel.productDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { product ->
                        fillInformation(product)
                        setUpPager(product.images as MutableList<ProductImage>)
                    }
                }
                is Resource.Loading -> {
                    Toast.makeText(context,
                        "Loading",
                        Toast.LENGTH_SHORT).show()
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

    private fun setUpPager(images: MutableList<ProductImage>) {
        images.add(images[0])
        images.add(images[0])
        images.add(images[0])

        val pager = binding.viewpager
        val adapter: PagerAdapter = ImageSliderAdapter(requireContext(), images)
        pager.adapter = adapter

        setUpTabLayout(pager)
    }

    private fun setUpTabLayout(pager: ViewPager) {
        val tabLayout = baseActivity.findViewById(R.id.tab_layout) as TabLayout
        tabLayout.visibility = View.VISIBLE
        tabLayout.setupWithViewPager(pager, true)
    }

    private fun fillInformation(product: Product) {
        binding.tvProductDetailsInfo.text = product.details
        binding.tvProductDetailsName.text = product.name
        binding.tvProductPrice.text =
            String.format(requireContext().getString(R.string.product_price),
                product.price.toString())
        binding.tvProductPriceSecond.text =
            String.format(requireContext().getString(R.string.product_price),
                product.price.toString())
        binding.tvProductDetailsSpecs.text = "response.data?.price"
    }

    override fun onDestroy() {
        super.onDestroy()
        baseActivity.restoreToolbar()
    }

}