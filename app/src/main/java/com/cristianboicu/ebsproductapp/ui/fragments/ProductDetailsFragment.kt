package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cristianboicu.ebsproductapp.databinding.FragmentProductDetailsBinding


class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        val productId = ProductDetailsFragmentArgs.fromBundle(requireArguments()).productId
        binding.tvId.text = productId.toString()
        return binding.root
    }

}