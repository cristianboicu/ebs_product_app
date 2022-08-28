package com.cristianboicu.ebsproductapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.ProductDomainModel
import com.cristianboicu.ebsproductapp.databinding.ItemProductBinding

class ProductsAdapter(
    context: Context,
    private var products: MutableList<ProductDomainModel>,
    val productClickListener: (productId: Long) -> Unit,
    val likeClickListener: (product: ProductDomainModel) -> Unit,
) :
    ArrayAdapter<ProductDomainModel>(context, R.layout.item_product, products) {

    fun submitList(items: List<ProductDomainModel>) {
        products.clear()
        products.addAll(items)
        notifyDataSetChanged()
    }

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

        populateView(binding, products[position])

        return binding.root
    }

    private fun populateView(binding: ItemProductBinding, product: ProductDomainModel) {
        binding.tvProductName.text = product.name
        binding.tvProductDetails.text = product.details
        binding.tvProductPrice.text = String.format(context.getString(R.string.product_price),
            product.price.toString())
        binding.tvProductPriceSecond.text = String.format(context.getString(R.string.product_price),
            product.price.toString())
        Glide.with(context).load(product.mainImage)
            .into(binding.ivProduct)
        binding.btnLike.isSelected = product.favorite

        binding.root.setOnClickListener {
            productClickListener(product.id)
        }
        binding.btnLike.setOnClickListener {
            it.isSelected = !it.isSelected
            likeClickListener(product)
        }
    }
}