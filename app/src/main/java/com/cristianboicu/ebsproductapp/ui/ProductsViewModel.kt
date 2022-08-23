package com.cristianboicu.ebsproductapp.ui

import androidx.lifecycle.ViewModel
import com.cristianboicu.ebsproductapp.data.repository.IDefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val repository: IDefaultRepository) :
    ViewModel() {

}