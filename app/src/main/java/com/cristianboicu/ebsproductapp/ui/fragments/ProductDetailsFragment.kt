package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.Product
import com.cristianboicu.ebsproductapp.databinding.FragmentProductDetailsBinding
import com.cristianboicu.ebsproductapp.ui.viewModels.ProductDetailsViewModel
import com.cristianboicu.ebsproductapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding
    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        val productId = ProductDetailsFragmentArgs.fromBundle(requireArguments()).productId

        viewModel.getProductDetails(productId)

        viewModel.productDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { product ->
                        fillInformation(product)
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

    private fun fillInformation(product: Product) {
        binding.tvProductDetailsInfo.text = product.details
        context?.let {
            Glide.with(it).load(product.mainImage)
                .into(binding.ivMainProduct)
        }
        binding.tvProductDetailsName.text = product.name
        binding.tvProductPrice.text =
            String.format(requireContext().getString(R.string.product_price),
                product.price.toString())
        binding.tvProductPriceSecond.text =
            String.format(requireContext().getString(R.string.product_price),
                product.price.toString())
        binding.tvProductDetailsSpecs.text = "response.data?.price"
    }

}