package com.cristianboicu.ebsproductapp

import android.widget.AbsListView
import com.cristianboicu.ebsproductapp.ui.ProductsViewModel

class EndlessScrollListener(private val viewModel: ProductsViewModel) :
    AbsListView.OnScrollListener {
    private var visibleThreshold = 4
    private var previousTotal = 0
    private var loading = true
    private var isLastPage = false

    fun setIsLastPage(lastPage: Boolean) {
        isLastPage = lastPage
    }

    override fun onScrollStateChanged(p0: AbsListView?, scrollState: Int) {
    }

    override fun onScroll(
        p0: AbsListView?,
        firstVisibleItem: Int,
        visibleItemCount: Int,
        totalItemCount: Int,
    ) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) && !isLastPage) {
            viewModel.getAllProducts()
            loading = true
        }
    }
}