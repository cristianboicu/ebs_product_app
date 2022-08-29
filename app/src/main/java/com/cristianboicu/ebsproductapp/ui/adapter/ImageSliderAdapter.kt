package com.cristianboicu.ebsproductapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.cristianboicu.ebsproductapp.R
import com.cristianboicu.ebsproductapp.data.model.ProductImage

class ImageSliderAdapter(private val context: Context, private var imageList: List<ProductImage>) : PagerAdapter() {
    override fun getCount(): Int {
        return imageList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =  (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.item_image_slide, container, false)
        val ivImages = view.findViewById<ImageView>(R.id.iv_main_product)

        imageList[position].let {
            Glide.with(context)
                .load(it.image)
                .into(ivImages)
        }


        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}