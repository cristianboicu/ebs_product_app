package com.cristianboicu.ebsproductapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cristianboicu.ebsproductapp.databinding.FragmentFavoritesBinding
import com.cristianboicu.ebsproductapp.ui.MainActivity


class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        setUpToolbar()
        return binding.root
    }

    private fun setUpToolbar() {
        val baseActivity = requireActivity() as MainActivity
        val toolbar = binding.layoutToolbar.toolbar
        baseActivity.setUpToolBar(toolbar)
    }

}