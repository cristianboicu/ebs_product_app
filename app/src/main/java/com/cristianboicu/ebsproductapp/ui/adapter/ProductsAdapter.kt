package com.cristianboicu.ebsproductapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.Product
import com.cristianboicu.ebsproductapp.databinding.ItemProductBinding

class ProductsAdapter(context: Context, private var products: List<Product>) :
    ArrayAdapter<Product>(context, R.layout.item_product, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var binding: ItemProductBinding

        if (convertView == null) {
            binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            binding.root.tag = binding
        } else {
            binding = convertView.tag as ItemProductBinding
        }

        binding.tvProductName.text = products[position].name
        binding.tvProductDetails.text = products[position].details
        binding.tvProductPrice.text = String.format(context.getString(R.string.product_price),
            products[position].price.toString())
        binding.tvProductPriceSecond.text = String.format(context.getString(R.string.product_price),
            products[position].price.toString())
        Glide.with(context).load(products[position].mainImage)
            .into(binding.ivProduct)

        return binding.root
    }
}